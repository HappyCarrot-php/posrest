package com.restaurante.dao;

import com.restaurante.model.Ticket;
import com.restaurante.util.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar operaciones CRUD de tickets.
 */
public class TicketDAO {
    
    /**
     * Inserta un nuevo ticket en la base de datos.
     * 
     * @param ticket ticket a insertar
     * @return true si se insertó correctamente, false en caso contrario
     */
    public boolean insertar(Ticket ticket) {
        String sql = "INSERT INTO tickets (id_venta, folio, total, cambio) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ticket.getIdVenta());
            stmt.setString(2, ticket.getFolio());
            stmt.setDouble(3, ticket.getTotal());
            stmt.setDouble(4, ticket.getCambio());
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar ticket: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Actualiza un ticket existente.
     * 
     * @param ticket ticket con datos actualizados
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizar(Ticket ticket) {
        String sql = "UPDATE tickets SET id_venta = ?, folio = ?, total = ?, cambio = ? WHERE id = ?";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ticket.getIdVenta());
            stmt.setString(2, ticket.getFolio());
            stmt.setDouble(3, ticket.getTotal());
            stmt.setDouble(4, ticket.getCambio());
            stmt.setInt(5, ticket.getId());
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar ticket: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Elimina un ticket por su ID.
     * 
     * @param id ID del ticket a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM tickets WHERE id = ?";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar ticket: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Busca un ticket por su ID.
     * 
     * @param id ID del ticket
     * @return ticket encontrado, o null si no existe
     */
    public Ticket buscarPorId(int id) {
        String sql = "SELECT * FROM tickets WHERE id = ?";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extraerTicket(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar ticket: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Busca un ticket por su folio.
     * 
     * @param folio folio del ticket
     * @return ticket encontrado, o null si no existe
     */
    public Ticket buscarPorFolio(String folio) {
        String sql = "SELECT * FROM tickets WHERE folio = ?";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, folio);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extraerTicket(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar ticket por folio: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Busca un ticket por el ID de venta.
     * 
     * @param idVenta ID de la venta
     * @return ticket encontrado, o null si no existe
     */
    public Ticket buscarPorVenta(int idVenta) {
        String sql = "SELECT * FROM tickets WHERE id_venta = ?";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idVenta);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extraerTicket(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar ticket por venta: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Obtiene todos los tickets.
     * 
     * @return lista de todos los tickets
     */
    public List<Ticket> obtenerTodos() {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM tickets ORDER BY fecha_emision DESC";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                tickets.add(extraerTicket(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener tickets: " + e.getMessage());
        }
        
        return tickets;
    }
    
    /**
     * Genera un folio único para un ticket.
     * 
     * @return folio único en formato "TICK-YYYYMMDD-NNNN"
     */
    public String generarFolio() {
        String sql = "SELECT COUNT(*) as total FROM tickets WHERE DATE(fecha_emision) = CURRENT_DATE";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                int consecutivo = rs.getInt("total") + 1;
                java.time.LocalDate hoy = java.time.LocalDate.now();
                String fecha = hoy.toString().replace("-", "");
                return String.format("TICK-%s-%04d", fecha, consecutivo);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al generar folio: " + e.getMessage());
        }
        
        // Folio por defecto en caso de error
        return "TICK-" + System.currentTimeMillis();
    }
    
    /**
     * Extrae un objeto Ticket desde un ResultSet.
     * 
     * @param rs ResultSet con datos del ticket
     * @return objeto Ticket
     * @throws SQLException si hay error al leer datos
     */
    private Ticket extraerTicket(ResultSet rs) throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setId(rs.getInt("id"));
        ticket.setIdVenta(rs.getInt("id_venta"));
        ticket.setFolio(rs.getString("folio"));
        ticket.setTotal(rs.getDouble("total"));
        ticket.setCambio(rs.getDouble("cambio"));
        ticket.setFechaEmision(rs.getTimestamp("fecha_emision").toLocalDateTime());
        return ticket;
    }
}
