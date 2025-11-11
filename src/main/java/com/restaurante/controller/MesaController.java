package com.restaurante.controller;

import com.restaurante.dao.MesaDAO;
import com.restaurante.dao.RespaldoDAO;
import com.restaurante.model.Mesa;
import com.restaurante.util.Validaciones;
import java.util.List;

/**
 * Controlador para gestionar la lógica de negocio de mesas.
 */
public class MesaController {
    
    private final MesaDAO mesaDAO;
    private final RespaldoDAO respaldoDAO;
    
    public MesaController() {
        this.mesaDAO = new MesaDAO();
        this.respaldoDAO = new RespaldoDAO();
    }
    
    /**
     * Crea una nueva mesa.
     * 
     * @param numero número de la mesa
     * @param estado estado inicial de la mesa
     * @return true si se creó correctamente, false en caso contrario
     */
    public boolean crearMesa(int numero, String estado) {
        // Validaciones
        if (!Validaciones.esPositivo(numero)) {
            System.err.println("Número de mesa debe ser mayor a cero");
            return false;
        }
        
        if (!Validaciones.esEstadoMesaValido(estado)) {
            System.err.println("Estado de mesa inválido");
            return false;
        }
        
        // Verificar si el número ya existe
        if (mesaDAO.buscarPorNumero(numero) != null) {
            System.err.println("Ya existe una mesa con ese número");
            return false;
        }
        
        // Crear mesa
        Mesa mesa = new Mesa(numero, estado.toLowerCase());
        boolean resultado = mesaDAO.insertar(mesa);
        
        if (resultado) {
            respaldoDAO.registrarOperacion("CREAR_MESA", 
                "Nueva mesa creada: Mesa " + numero);
        }
        
        return resultado;
    }
    
    /**
     * Actualiza una mesa existente.
     * 
     * @param mesa mesa con datos actualizados
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizarMesa(Mesa mesa) {
        // Validaciones
        if (!Validaciones.esPositivo(mesa.getNumero())) {
            System.err.println("Número de mesa debe ser mayor a cero");
            return false;
        }
        
        if (!Validaciones.esEstadoMesaValido(mesa.getEstado())) {
            System.err.println("Estado de mesa inválido");
            return false;
        }
        
        boolean resultado = mesaDAO.actualizar(mesa);
        
        if (resultado) {
            respaldoDAO.registrarOperacion("ACTUALIZAR_MESA", 
                "Mesa actualizada: Mesa " + mesa.getNumero() + " (ID: " + mesa.getId() + ")");
        }
        
        return resultado;
    }
    
    /**
     * Cambia el estado de una mesa.
     * 
     * @param id ID de la mesa
     * @param nuevoEstado nuevo estado de la mesa
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean cambiarEstadoMesa(int id, String nuevoEstado) {
        if (!Validaciones.esEstadoMesaValido(nuevoEstado)) {
            System.err.println("Estado de mesa inválido");
            return false;
        }
        
        Mesa mesa = mesaDAO.buscarPorId(id);
        if (mesa == null) {
            System.err.println("Mesa no encontrada");
            return false;
        }
        
        boolean resultado = mesaDAO.actualizarEstado(id, nuevoEstado.toLowerCase());
        
        if (resultado) {
            respaldoDAO.registrarOperacion("CAMBIAR_ESTADO_MESA", 
                "Mesa " + mesa.getNumero() + " cambió a estado: " + nuevoEstado);
        }
        
        return resultado;
    }
    
    /**
     * Marca una mesa como ocupada.
     * 
     * @param id ID de la mesa
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean ocuparMesa(int id) {
        return cambiarEstadoMesa(id, "ocupada");
    }
    
    /**
     * Marca una mesa como libre.
     * 
     * @param id ID de la mesa
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean liberarMesa(int id) {
        return cambiarEstadoMesa(id, "libre");
    }
    
    /**
     * Marca una mesa como reservada.
     * 
     * @param id ID de la mesa
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean reservarMesa(int id) {
        return cambiarEstadoMesa(id, "reservada");
    }
    
    /**
     * Elimina una mesa por su ID.
     * 
     * @param id ID de la mesa a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminarMesa(int id) {
        Mesa mesa = mesaDAO.buscarPorId(id);
        
        if (mesa == null) {
            System.err.println("Mesa no encontrada");
            return false;
        }
        
        boolean resultado = mesaDAO.eliminar(id);
        
        if (resultado) {
            respaldoDAO.registrarOperacion("ELIMINAR_MESA", 
                "Mesa eliminada: Mesa " + mesa.getNumero() + " (ID: " + id + ")");
        }
        
        return resultado;
    }
    
    /**
     * Obtiene todas las mesas.
     * 
     * @return lista de todas las mesas
     */
    public List<Mesa> obtenerTodasLasMesas() {
        return mesaDAO.obtenerTodas();
    }
    
    /**
     * Obtiene mesas por estado.
     * 
     * @param estado estado a filtrar
     * @return lista de mesas con ese estado
     */
    public List<Mesa> obtenerMesasPorEstado(String estado) {
        return mesaDAO.obtenerPorEstado(estado.toLowerCase());
    }
    
    /**
     * Obtiene mesas libres.
     * 
     * @return lista de mesas libres
     */
    public List<Mesa> obtenerMesasLibres() {
        return mesaDAO.obtenerPorEstado("libre");
    }
    
    /**
     * Obtiene mesas ocupadas.
     * 
     * @return lista de mesas ocupadas
     */
    public List<Mesa> obtenerMesasOcupadas() {
        return mesaDAO.obtenerPorEstado("ocupada");
    }
    
    /**
     * Busca una mesa por su ID.
     * 
     * @param id ID de la mesa
     * @return mesa encontrada, o null si no existe
     */
    public Mesa buscarMesaPorId(int id) {
        return mesaDAO.buscarPorId(id);
    }
    
    /**
     * Busca una mesa por su número.
     * 
     * @param numero número de la mesa
     * @return mesa encontrada, o null si no existe
     */
    public Mesa buscarMesaPorNumero(int numero) {
        return mesaDAO.buscarPorNumero(numero);
    }
}
