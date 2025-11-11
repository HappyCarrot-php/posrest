package com.restaurante.dao;

import com.restaurante.model.DetalleVenta;
import com.restaurante.util.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar operaciones CRUD de detalle de ventas.
 */
public class DetalleVentaDAO {
    
    /**
     * Inserta un nuevo detalle de venta en la base de datos.
     * 
     * @param detalle detalle de venta a insertar
     * @return true si se insertó correctamente, false en caso contrario
     */
    public boolean insertar(DetalleVenta detalle) {
        String sql = "INSERT INTO detalle_ventas (id_venta, id_producto, cantidad, precio_unitario, subtotal) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, detalle.getIdVenta());
            stmt.setInt(2, detalle.getIdProducto());
            stmt.setInt(3, detalle.getCantidad());
            stmt.setDouble(4, detalle.getPrecioUnitario());
            stmt.setDouble(5, detalle.getSubtotal());
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar detalle de venta: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Inserta múltiples detalles de venta en una sola transacción.
     * 
     * @param detalles lista de detalles a insertar
     * @return true si todos se insertaron correctamente, false en caso contrario
     */
    public boolean insertarLote(List<DetalleVenta> detalles) {
        String sql = "INSERT INTO detalle_ventas (id_venta, id_producto, cantidad, precio_unitario, subtotal) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        Connection conn = null;
        try {
            conn = ConexionDB.obtenerConexion();
            conn.setAutoCommit(false);
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                for (DetalleVenta detalle : detalles) {
                    stmt.setInt(1, detalle.getIdVenta());
                    stmt.setInt(2, detalle.getIdProducto());
                    stmt.setInt(3, detalle.getCantidad());
                    stmt.setDouble(4, detalle.getPrecioUnitario());
                    stmt.setDouble(5, detalle.getSubtotal());
                    stmt.addBatch();
                }
                
                stmt.executeBatch();
                conn.commit();
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al insertar lote de detalles: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error al hacer rollback: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    System.err.println("Error al restablecer autocommit: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Actualiza un detalle de venta existente.
     * 
     * @param detalle detalle con datos actualizados
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizar(DetalleVenta detalle) {
        String sql = "UPDATE detalle_ventas SET id_venta = ?, id_producto = ?, cantidad = ?, " +
                     "precio_unitario = ?, subtotal = ? WHERE id = ?";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, detalle.getIdVenta());
            stmt.setInt(2, detalle.getIdProducto());
            stmt.setInt(3, detalle.getCantidad());
            stmt.setDouble(4, detalle.getPrecioUnitario());
            stmt.setDouble(5, detalle.getSubtotal());
            stmt.setInt(6, detalle.getId());
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar detalle de venta: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Elimina un detalle de venta por su ID.
     * 
     * @param id ID del detalle a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM detalle_ventas WHERE id = ?";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar detalle de venta: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Busca un detalle de venta por su ID.
     * 
     * @param id ID del detalle
     * @return detalle encontrado, o null si no existe
     */
    public DetalleVenta buscarPorId(int id) {
        String sql = "SELECT * FROM detalle_ventas WHERE id = ?";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extraerDetalle(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar detalle de venta: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Obtiene todos los detalles de una venta específica.
     * 
     * @param idVenta ID de la venta
     * @return lista de detalles de la venta
     */
    public List<DetalleVenta> obtenerPorVenta(int idVenta) {
        List<DetalleVenta> detalles = new ArrayList<>();
        String sql = "SELECT * FROM detalle_ventas WHERE id_venta = ? ORDER BY id";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idVenta);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    detalles.add(extraerDetalle(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener detalles por venta: " + e.getMessage());
        }
        
        return detalles;
    }
    
    /**
     * Obtiene todos los detalles de venta.
     * 
     * @return lista de todos los detalles
     */
    public List<DetalleVenta> obtenerTodos() {
        List<DetalleVenta> detalles = new ArrayList<>();
        String sql = "SELECT * FROM detalle_ventas ORDER BY id_venta, id";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                detalles.add(extraerDetalle(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener detalles de venta: " + e.getMessage());
        }
        
        return detalles;
    }
    
    /**
     * Extrae un objeto DetalleVenta desde un ResultSet.
     * 
     * @param rs ResultSet con datos del detalle
     * @return objeto DetalleVenta
     * @throws SQLException si hay error al leer datos
     */
    private DetalleVenta extraerDetalle(ResultSet rs) throws SQLException {
        DetalleVenta detalle = new DetalleVenta();
        detalle.setId(rs.getInt("id"));
        detalle.setIdVenta(rs.getInt("id_venta"));
        detalle.setIdProducto(rs.getInt("id_producto"));
        detalle.setCantidad(rs.getInt("cantidad"));
        detalle.setPrecioUnitario(rs.getDouble("precio_unitario"));
        detalle.setSubtotal(rs.getDouble("subtotal"));
        return detalle;
    }
}
