package com.restaurante.controller;

import com.restaurante.dao.*;
import com.restaurante.model.*;
import com.restaurante.util.FinancialUtils;
import com.restaurante.util.FinancialUtils.TotalesVenta;
import com.restaurante.util.Validaciones;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para gestionar la lógica de negocio de ventas.
 */
public class VentaController {
    
    private final VentaDAO ventaDAO;
    private final DetalleVentaDAO detalleVentaDAO;
    private final TicketDAO ticketDAO;
    private final ProductoDAO productoDAO;
    private final MesaDAO mesaDAO;
    private final RespaldoDAO respaldoDAO;
    
    public VentaController() {
        this.ventaDAO = new VentaDAO();
        this.detalleVentaDAO = new DetalleVentaDAO();
        this.ticketDAO = new TicketDAO();
        this.productoDAO = new ProductoDAO();
        this.mesaDAO = new MesaDAO();
        this.respaldoDAO = new RespaldoDAO();
    }
    
    /**
     * Clase interna para representar un ítem del carrito de venta.
     */
    public static class ItemVenta {
        private Producto producto;
        private int cantidad;
        
        public ItemVenta(Producto producto, int cantidad) {
            this.producto = producto;
            this.cantidad = cantidad;
        }
        
        public Producto getProducto() {
            return producto;
        }
        
        public int getCantidad() {
            return cantidad;
        }
        
        public void setCantidad(int cantidad) {
            this.cantidad = cantidad;
        }
        
        public double getSubtotal() {
            return producto.getPrecio() * cantidad;
        }
    }
    
    /**
     * Registra una nueva venta completa con sus detalles y ticket.
     * 
     * @param idUsuario ID del usuario que realiza la venta
     * @param idMesa ID de la mesa (puede ser null para ventas sin mesa)
     * @param items lista de productos y cantidades
     * @param montoPagado monto pagado por el cliente
     * @return folio del ticket generado, o null si hubo error
     */
    public String registrarVenta(int idUsuario, Integer idMesa, List<ItemVenta> items, double montoPagado) {
        // Validaciones
        if (items == null || items.isEmpty()) {
            System.err.println("No hay productos en la venta");
            return null;
        }
        
        // Calcular total
        TotalesVenta totales = FinancialUtils.calcularTotales(items);
        double total = totales.getTotal();
        
        if (!Validaciones.esPositivo(total)) {
            System.err.println("El total debe ser mayor a cero");
            return null;
        }
        
        if (montoPagado < total) {
            System.err.println("El monto pagado es insuficiente");
            return null;
        }
        
        // Verificar disponibilidad de productos
        for (ItemVenta item : items) {
            Producto producto = productoDAO.buscarPorId(item.getProducto().getId());
            if (producto == null || !producto.isDisponible()) {
                System.err.println("Producto no disponible: " + item.getProducto().getNombre());
                return null;
            }
        }
        
        try {
            // 1. Crear la venta
            Venta venta = new Venta(idUsuario, idMesa, total);
            int idVenta = ventaDAO.insertar(venta);
            
            if (idVenta == -1) {
                System.err.println("Error al crear la venta");
                return null;
            }
            
            // 2. Crear los detalles de venta
            List<DetalleVenta> detalles = new ArrayList<>();
            for (ItemVenta item : items) {
                DetalleVenta detalle = new DetalleVenta(
                    idVenta,
                    item.getProducto().getId(),
                    item.getCantidad(),
                    item.getProducto().getPrecio(),
                    item.getSubtotal()
                );
                detalles.add(detalle);
            }
            
            if (!detalleVentaDAO.insertarLote(detalles)) {
                System.err.println("Error al crear detalles de venta");
                return null;
            }
            
            // 3. Generar ticket
            String folio = ticketDAO.generarFolio();
            double cambio = FinancialUtils.redondear(montoPagado - total);
            
            Ticket ticket = new Ticket(idVenta, folio, total, cambio);
            if (!ticketDAO.insertar(ticket)) {
                System.err.println("Error al generar ticket");
                return null;
            }
            
            // 4. Si hay mesa, marcarla como ocupada
            if (idMesa != null) {
                mesaDAO.actualizarEstado(idMesa, "ocupada");
            }
            
            // 5. Registrar en auditoría
            respaldoDAO.registrarOperacion("VENTA_REGISTRADA", 
                "Venta #" + idVenta + " registrada. Total: $" + total + " - Folio: " + folio);
            
            return folio;
            
        } catch (Exception e) {
            System.err.println("Error al registrar venta: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Calcula el total de una lista de items.
     * 
     * @param items lista de items
     * @return total calculado
     */
    public double calcularTotal(List<ItemVenta> items) {
        return FinancialUtils.calcularTotales(items).getTotal();
    }

    /**
     * Calcula el subtotal sin impuestos.
     */
    public double calcularSubtotal(List<ItemVenta> items) {
        return FinancialUtils.calcularTotales(items).getSubtotal();
    }

    /**
     * Calcula el IVA correspondiente a los ítems.
     */
    public double calcularIva(List<ItemVenta> items) {
        return FinancialUtils.calcularTotales(items).getIva();
    }
    
    /**
     * Calcula el cambio a devolver.
     * 
     * @param total total de la venta
     * @param montoPagado monto pagado por el cliente
     * @return cambio a devolver
     */
    public double calcularCambio(double total, double montoPagado) {
        return FinancialUtils.redondear(montoPagado - total);
    }
    
    /**
     * Obtiene todas las ventas.
     * 
     * @return lista de todas las ventas
     */
    public List<Venta> obtenerTodasLasVentas() {
        return ventaDAO.obtenerTodas();
    }
    
    /**
     * Obtiene ventas por usuario.
     * 
     * @param idUsuario ID del usuario
     * @return lista de ventas del usuario
     */
    public List<Venta> obtenerVentasPorUsuario(int idUsuario) {
        return ventaDAO.obtenerPorUsuario(idUsuario);
    }
    
    /**
     * Obtiene ventas en un rango de fechas.
     * 
     * @param fechaInicio fecha de inicio
     * @param fechaFin fecha de fin
     * @return lista de ventas en el rango
     */
    public List<Venta> obtenerVentasPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return ventaDAO.obtenerPorFechas(fechaInicio, fechaFin);
    }
    
    /**
     * Calcula el total de ventas en un período.
     * 
     * @param fechaInicio fecha de inicio
     * @param fechaFin fecha de fin
     * @return total de ventas
     */
    public double calcularTotalVentas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return ventaDAO.calcularTotalPorFechas(fechaInicio, fechaFin);
    }
    
    /**
     * Obtiene los detalles de una venta.
     * 
     * @param idVenta ID de la venta
     * @return lista de detalles de la venta
     */
    public List<DetalleVenta> obtenerDetallesVenta(int idVenta) {
        return detalleVentaDAO.obtenerPorVenta(idVenta);
    }
    
    /**
     * Busca una venta por su ID.
     * 
     * @param id ID de la venta
     * @return venta encontrada, o null si no existe
     */
    public Venta buscarVentaPorId(int id) {
        return ventaDAO.buscarPorId(id);
    }
    
    /**
     * Busca el ticket de una venta.
     * 
     * @param idVenta ID de la venta
     * @return ticket encontrado, o null si no existe
     */
    public Ticket obtenerTicketPorVenta(int idVenta) {
        return ticketDAO.buscarPorVenta(idVenta);
    }
    
    /**
     * Busca un ticket por su folio.
     * 
     * @param folio folio del ticket
     * @return ticket encontrado, o null si no existe
     */
    public Ticket buscarTicketPorFolio(String folio) {
        return ticketDAO.buscarPorFolio(folio);
    }
}
