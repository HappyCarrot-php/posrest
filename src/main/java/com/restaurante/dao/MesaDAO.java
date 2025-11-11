package com.restaurante.dao;

import com.restaurante.model.Mesa;
import com.restaurante.util.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar operaciones CRUD de mesas.
 */
public class MesaDAO {
    
    /**
     * Inserta una nueva mesa en la base de datos.
     * 
     * @param mesa mesa a insertar
     * @return true si se insertó correctamente, false en caso contrario
     */
    public boolean insertar(Mesa mesa) {
        String sql = "INSERT INTO mesas (numero, estado) VALUES (?, ?)";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, mesa.getNumero());
            stmt.setString(2, mesa.getEstado());
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar mesa: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Actualiza una mesa existente.
     * 
     * @param mesa mesa con datos actualizados
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizar(Mesa mesa) {
        String sql = "UPDATE mesas SET numero = ?, estado = ? WHERE id = ?";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, mesa.getNumero());
            stmt.setString(2, mesa.getEstado());
            stmt.setInt(3, mesa.getId());
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar mesa: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Actualiza solo el estado de una mesa.
     * 
     * @param id ID de la mesa
     * @param nuevoEstado nuevo estado de la mesa
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizarEstado(int id, String nuevoEstado) {
        String sql = "UPDATE mesas SET estado = ? WHERE id = ?";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, id);
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado de mesa: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Elimina una mesa por su ID.
     * 
     * @param id ID de la mesa a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM mesas WHERE id = ?";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar mesa: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Busca una mesa por su ID.
     * 
     * @param id ID de la mesa
     * @return mesa encontrada, o null si no existe
     */
    public Mesa buscarPorId(int id) {
        String sql = "SELECT * FROM mesas WHERE id = ?";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extraerMesa(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar mesa: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Busca una mesa por su número.
     * 
     * @param numero número de la mesa
     * @return mesa encontrada, o null si no existe
     */
    public Mesa buscarPorNumero(int numero) {
        String sql = "SELECT * FROM mesas WHERE numero = ?";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, numero);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extraerMesa(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar mesa por número: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Obtiene todas las mesas.
     * 
     * @return lista de todas las mesas
     */
    public List<Mesa> obtenerTodas() {
        List<Mesa> mesas = new ArrayList<>();
        String sql = "SELECT * FROM mesas ORDER BY numero";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                mesas.add(extraerMesa(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener mesas: " + e.getMessage());
        }
        
        return mesas;
    }
    
    /**
     * Obtiene mesas filtradas por estado.
     * 
     * @param estado estado a filtrar
     * @return lista de mesas con ese estado
     */
    public List<Mesa> obtenerPorEstado(String estado) {
        List<Mesa> mesas = new ArrayList<>();
        String sql = "SELECT * FROM mesas WHERE estado = ? ORDER BY numero";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, estado);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    mesas.add(extraerMesa(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener mesas por estado: " + e.getMessage());
        }
        
        return mesas;
    }
    
    /**
     * Extrae un objeto Mesa desde un ResultSet.
     * 
     * @param rs ResultSet con datos de la mesa
     * @return objeto Mesa
     * @throws SQLException si hay error al leer datos
     */
    private Mesa extraerMesa(ResultSet rs) throws SQLException {
        Mesa mesa = new Mesa();
        mesa.setId(rs.getInt("id"));
        mesa.setNumero(rs.getInt("numero"));
        mesa.setEstado(rs.getString("estado"));
        return mesa;
    }
}
