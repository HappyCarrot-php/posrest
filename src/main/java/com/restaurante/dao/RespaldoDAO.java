package com.restaurante.dao;

import com.restaurante.model.Respaldo;
import com.restaurante.util.ConexionDB;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar operaciones de auditoría y respaldo.
 */
public class RespaldoDAO {
    
    /**
     * Inserta un nuevo registro de auditoría.
     * 
     * @param respaldo registro a insertar
     * @return true si se insertó correctamente, false en caso contrario
     */
    public boolean insertar(Respaldo respaldo) {
        String sql = "INSERT INTO respaldo (tipo_operacion, descripcion) VALUES (?, ?)";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, respaldo.getTipoOperacion());
            stmt.setString(2, respaldo.getDescripcion());
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar registro de respaldo: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Registra una operación de forma simplificada.
     * 
     * @param tipoOperacion tipo de operación realizada
     * @param descripcion descripción detallada de la operación
     * @return true si se registró correctamente, false en caso contrario
     */
    public boolean registrarOperacion(String tipoOperacion, String descripcion) {
        Respaldo respaldo = new Respaldo(tipoOperacion, descripcion);
        return insertar(respaldo);
    }
    
    /**
     * Elimina un registro de respaldo por su ID.
     * 
     * @param id ID del registro a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM respaldo WHERE id = ?";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar registro de respaldo: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Busca un registro de respaldo por su ID.
     * 
     * @param id ID del registro
     * @return registro encontrado, o null si no existe
     */
    public Respaldo buscarPorId(int id) {
        String sql = "SELECT * FROM respaldo WHERE id = ?";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extraerRespaldo(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar registro de respaldo: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Obtiene todos los registros de respaldo.
     * 
     * @return lista de todos los registros
     */
    public List<Respaldo> obtenerTodos() {
        List<Respaldo> registros = new ArrayList<>();
        String sql = "SELECT * FROM respaldo ORDER BY fecha DESC";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                registros.add(extraerRespaldo(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener registros de respaldo: " + e.getMessage());
        }
        
        return registros;
    }
    
    /**
     * Obtiene registros filtrados por tipo de operación.
     * 
     * @param tipoOperacion tipo de operación a filtrar
     * @return lista de registros del tipo especificado
     */
    public List<Respaldo> obtenerPorTipo(String tipoOperacion) {
        List<Respaldo> registros = new ArrayList<>();
        String sql = "SELECT * FROM respaldo WHERE tipo_operacion = ? ORDER BY fecha DESC";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, tipoOperacion);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    registros.add(extraerRespaldo(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener registros por tipo: " + e.getMessage());
        }
        
        return registros;
    }
    
    /**
     * Obtiene registros en un rango de fechas.
     * 
     * @param fechaInicio fecha de inicio
     * @param fechaFin fecha de fin
     * @return lista de registros en el rango
     */
    public List<Respaldo> obtenerPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<Respaldo> registros = new ArrayList<>();
        String sql = "SELECT * FROM respaldo WHERE fecha BETWEEN ? AND ? ORDER BY fecha DESC";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(fechaInicio));
            stmt.setTimestamp(2, Timestamp.valueOf(fechaFin));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    registros.add(extraerRespaldo(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener registros por fechas: " + e.getMessage());
        }
        
        return registros;
    }
    
    /**
     * Elimina registros más antiguos que una fecha específica.
     * 
     * @param fechaLimite fecha límite (se eliminan registros anteriores)
     * @return número de registros eliminados
     */
    public int limpiarAntiguos(LocalDateTime fechaLimite) {
        String sql = "DELETE FROM respaldo WHERE fecha < ?";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(fechaLimite));
            
            return stmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error al limpiar registros antiguos: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Extrae un objeto Respaldo desde un ResultSet.
     * 
     * @param rs ResultSet con datos del registro
     * @return objeto Respaldo
     * @throws SQLException si hay error al leer datos
     */
    private Respaldo extraerRespaldo(ResultSet rs) throws SQLException {
        Respaldo respaldo = new Respaldo();
        respaldo.setId(rs.getInt("id"));
        respaldo.setTipoOperacion(rs.getString("tipo_operacion"));
        respaldo.setDescripcion(rs.getString("descripcion"));
        respaldo.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
        return respaldo;
    }
}
