package com.restaurante.dao;

import com.restaurante.model.Venta;
import com.restaurante.util.ConexionDB;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar operaciones CRUD de ventas.
 */
public class VentaDAO {
    
    /**
     * Inserta una nueva venta en la base de datos y retorna su ID generado.
     * 
     * @param venta venta a insertar
     * @return ID de la venta insertada, o -1 si hubo error
     */
    public int insertar(Venta venta) {
        String sql = "INSERT INTO ventas (id_usuario, id_mesa, total) VALUES (?, ?, ?) RETURNING id";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, venta.getIdUsuario());
            
            if (venta.getIdMesa() != null) {
                stmt.setInt(2, venta.getIdMesa());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            
            stmt.setDouble(3, venta.getTotal());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al insertar venta: " + e.getMessage());
        }
        
        return -1;
    }
    
    /**
     * Actualiza una venta existente.
     * 
     * @param venta venta con datos actualizados
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizar(Venta venta) {
        String sql = "UPDATE ventas SET id_usuario = ?, id_mesa = ?, total = ? WHERE id = ?";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, venta.getIdUsuario());
            
            if (venta.getIdMesa() != null) {
                stmt.setInt(2, venta.getIdMesa());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            
            stmt.setDouble(3, venta.getTotal());
            stmt.setInt(4, venta.getId());
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar venta: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Elimina una venta por su ID.
     * 
     * @param id ID de la venta a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM ventas WHERE id = ?";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar venta: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Busca una venta por su ID.
     * 
     * @param id ID de la venta
     * @return venta encontrada, o null si no existe
     */
    public Venta buscarPorId(int id) {
        String sql = "SELECT * FROM ventas WHERE id = ?";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extraerVenta(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar venta: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Obtiene todas las ventas.
     * 
     * @return lista de todas las ventas
     */
    public List<Venta> obtenerTodas() {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM ventas ORDER BY fecha_venta DESC";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                ventas.add(extraerVenta(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener ventas: " + e.getMessage());
        }
        
        return ventas;
    }
    
    /**
     * Obtiene ventas por usuario.
     * 
     * @param idUsuario ID del usuario
     * @return lista de ventas del usuario
     */
    public List<Venta> obtenerPorUsuario(int idUsuario) {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM ventas WHERE id_usuario = ? ORDER BY fecha_venta DESC";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ventas.add(extraerVenta(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener ventas por usuario: " + e.getMessage());
        }
        
        return ventas;
    }
    
    /**
     * Obtiene ventas en un rango de fechas.
     * 
     * @param fechaInicio fecha de inicio
     * @param fechaFin fecha de fin
     * @return lista de ventas en el rango
     */
    public List<Venta> obtenerPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM ventas WHERE fecha_venta BETWEEN ? AND ? ORDER BY fecha_venta DESC";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(fechaInicio));
            stmt.setTimestamp(2, Timestamp.valueOf(fechaFin));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ventas.add(extraerVenta(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener ventas por fechas: " + e.getMessage());
        }
        
        return ventas;
    }
    
    /**
     * Calcula el total de ventas en un rango de fechas.
     * 
     * @param fechaInicio fecha de inicio
     * @param fechaFin fecha de fin
     * @return total de ventas
     */
    public double calcularTotalPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        String sql = "SELECT COALESCE(SUM(total), 0) as total FROM ventas WHERE fecha_venta BETWEEN ? AND ?";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(fechaInicio));
            stmt.setTimestamp(2, Timestamp.valueOf(fechaFin));
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al calcular total de ventas: " + e.getMessage());
        }
        
        return 0.0;
    }
    
    /**
     * Extrae un objeto Venta desde un ResultSet.
     * 
     * @param rs ResultSet con datos de la venta
     * @return objeto Venta
     * @throws SQLException si hay error al leer datos
     */
    private Venta extraerVenta(ResultSet rs) throws SQLException {
        Venta venta = new Venta();
        venta.setId(rs.getInt("id"));
        venta.setIdUsuario(rs.getInt("id_usuario"));
        
        int idMesa = rs.getInt("id_mesa");
        if (!rs.wasNull()) {
            venta.setIdMesa(idMesa);
        }
        
        venta.setTotal(rs.getDouble("total"));
        venta.setFechaVenta(rs.getTimestamp("fecha_venta").toLocalDateTime());
        return venta;
    }
}
