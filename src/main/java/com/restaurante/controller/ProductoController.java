package com.restaurante.controller;

import com.restaurante.dao.ProductoDAO;
import com.restaurante.dao.RespaldoDAO;
import com.restaurante.model.Producto;
import com.restaurante.util.Validaciones;
import java.util.List;

/**
 * Controlador para gestionar la lógica de negocio de productos.
 */
public class ProductoController {
    
    private final ProductoDAO productoDAO;
    private final RespaldoDAO respaldoDAO;
    
    public ProductoController() {
        this.productoDAO = new ProductoDAO();
        this.respaldoDAO = new RespaldoDAO();
    }
    
    /**
     * Crea un nuevo producto.
     * 
     * @param nombre nombre del producto
     * @param categoria categoría del producto
     * @param precio precio del producto
     * @param disponible si está disponible o no
     * @return true si se creó correctamente, false en caso contrario
     */
    public boolean crearProducto(String nombre, String categoria, double precio, boolean disponible) {
        // Validaciones
        if (!Validaciones.noEsVacio(nombre)) {
            System.err.println("Nombre del producto vacío");
            return false;
        }
        
        if (!Validaciones.noEsVacio(categoria)) {
            System.err.println("Categoría vacía");
            return false;
        }
        
        if (!Validaciones.esPositivo(precio)) {
            System.err.println("Precio debe ser mayor a cero");
            return false;
        }
        
        // Crear producto
        Producto producto = new Producto(nombre, categoria, precio, disponible);
        boolean resultado = productoDAO.insertar(producto);
        
        if (resultado) {
            respaldoDAO.registrarOperacion("CREAR_PRODUCTO", 
                "Nuevo producto creado: " + nombre + " - $" + precio);
        }
        
        return resultado;
    }
    
    /**
     * Actualiza un producto existente.
     * 
     * @param producto producto con datos actualizados
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizarProducto(Producto producto) {
        // Validaciones
        if (!Validaciones.noEsVacio(producto.getNombre())) {
            System.err.println("Nombre del producto vacío");
            return false;
        }
        
        if (!Validaciones.noEsVacio(producto.getCategoria())) {
            System.err.println("Categoría vacía");
            return false;
        }
        
        if (!Validaciones.esPositivo(producto.getPrecio())) {
            System.err.println("Precio debe ser mayor a cero");
            return false;
        }
        
        boolean resultado = productoDAO.actualizar(producto);
        
        if (resultado) {
            respaldoDAO.registrarOperacion("ACTUALIZAR_PRODUCTO", 
                "Producto actualizado: " + producto.getNombre() + " (ID: " + producto.getId() + ")");
        }
        
        return resultado;
    }
    
    /**
     * Elimina un producto por su ID.
     * 
     * @param id ID del producto a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminarProducto(int id) {
        Producto producto = productoDAO.buscarPorId(id);
        
        if (producto == null) {
            System.err.println("Producto no encontrado");
            return false;
        }
        
        boolean resultado = productoDAO.eliminar(id);
        
        if (resultado) {
            respaldoDAO.registrarOperacion("ELIMINAR_PRODUCTO", 
                "Producto eliminado: " + producto.getNombre() + " (ID: " + id + ")");
        }
        
        return resultado;
    }
    
    /**
     * Cambia la disponibilidad de un producto.
     * 
     * @param id ID del producto
     * @param disponible nueva disponibilidad
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean cambiarDisponibilidad(int id, boolean disponible) {
        Producto producto = productoDAO.buscarPorId(id);
        
        if (producto == null) {
            System.err.println("Producto no encontrado");
            return false;
        }
        
        producto.setDisponible(disponible);
        boolean resultado = productoDAO.actualizar(producto);
        
        if (resultado) {
            String estado = disponible ? "disponible" : "no disponible";
            respaldoDAO.registrarOperacion("CAMBIAR_DISPONIBILIDAD", 
                "Producto " + producto.getNombre() + " marcado como " + estado);
        }
        
        return resultado;
    }
    
    /**
     * Obtiene todos los productos.
     * 
     * @return lista de todos los productos
     */
    public List<Producto> obtenerTodosLosProductos() {
        return productoDAO.obtenerTodos();
    }
    
    /**
     * Obtiene solo los productos disponibles.
     * 
     * @return lista de productos disponibles
     */
    public List<Producto> obtenerProductosDisponibles() {
        return productoDAO.obtenerDisponibles();
    }
    
    /**
     * Obtiene productos por categoría.
     * 
     * @param categoria categoría a filtrar
     * @return lista de productos de esa categoría
     */
    public List<Producto> obtenerProductosPorCategoria(String categoria) {
        return productoDAO.obtenerPorCategoria(categoria);
    }
    
    /**
     * Busca productos por nombre.
     * 
     * @param nombre nombre o parte del nombre
     * @return lista de productos que coinciden
     */
    public List<Producto> buscarProductosPorNombre(String nombre) {
        return productoDAO.buscarPorNombre(nombre);
    }
    
    /**
     * Busca un producto por su ID.
     * 
     * @param id ID del producto
     * @return producto encontrado, o null si no existe
     */
    public Producto buscarProductoPorId(int id) {
        return productoDAO.buscarPorId(id);
    }
}
