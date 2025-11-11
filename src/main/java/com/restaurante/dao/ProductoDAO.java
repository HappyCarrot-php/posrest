package com.restaurante.dao;

import com.restaurante.model.Producto;
import com.restaurante.util.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar operaciones CRUD de productos.
 */
public class ProductoDAO {
    
    /**
     * Inserta un nuevo producto en la base de datos.
     * 
     * @param producto producto a insertar
     * @return true si se insertó correctamente, false en caso contrario
     */
    public boolean insertar(Producto producto) {
        String sql = "INSERT INTO productos (nombre, categoria, precio, disponible) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getCategoria());
            stmt.setDouble(3, producto.getPrecio());
            stmt.setBoolean(4, producto.isDisponible());
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar producto: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Actualiza un producto existente.
     * 
     * @param producto producto con datos actualizados
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizar(Producto producto) {
        String sql = "UPDATE productos SET nombre = ?, categoria = ?, precio = ?, disponible = ? WHERE id = ?";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getCategoria());
            stmt.setDouble(3, producto.getPrecio());
            stmt.setBoolean(4, producto.isDisponible());
            stmt.setInt(5, producto.getId());
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Elimina un producto por su ID.
     * 
     * @param id ID del producto a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM productos WHERE id = ?";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Busca un producto por su ID.
     * 
     * @param id ID del producto
     * @return producto encontrado, o null si no existe
     */
    public Producto buscarPorId(int id) {
        String sql = "SELECT * FROM productos WHERE id = ?";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extraerProducto(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar producto: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Obtiene todos los productos.
     * 
     * @return lista de todos los productos
     */
    public List<Producto> obtenerTodos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos ORDER BY categoria, nombre";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                productos.add(extraerProducto(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener productos: " + e.getMessage());
        }
        
        return productos;
    }
    
    /**
     * Obtiene productos disponibles.
     * 
     * @return lista de productos disponibles
     */
    public List<Producto> obtenerDisponibles() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE disponible = true ORDER BY categoria, nombre";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                productos.add(extraerProducto(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener productos disponibles: " + e.getMessage());
        }
        
        return productos;
    }
    
    /**
     * Obtiene productos filtrados por categoría.
     * 
     * @param categoria categoría a filtrar
     * @return lista de productos de esa categoría
     */
    public List<Producto> obtenerPorCategoria(String categoria) {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE categoria = ? ORDER BY nombre";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, categoria);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    productos.add(extraerProducto(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener productos por categoría: " + e.getMessage());
        }
        
        return productos;
    }
    
    /**
     * Busca productos por nombre (búsqueda parcial).
     * 
     * @param nombre nombre o parte del nombre a buscar
     * @return lista de productos que coinciden
     */
    public List<Producto> buscarPorNombre(String nombre) {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE nombre ILIKE ? ORDER BY nombre";
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nombre + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    productos.add(extraerProducto(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar productos por nombre: " + e.getMessage());
        }
        
        return productos;
    }
    
    /**
     * Extrae un objeto Producto desde un ResultSet.
     * 
     * @param rs ResultSet con datos del producto
     * @return objeto Producto
     * @throws SQLException si hay error al leer datos
     */
    private Producto extraerProducto(ResultSet rs) throws SQLException {
        Producto producto = new Producto();
        producto.setId(rs.getInt("id"));
        producto.setNombre(rs.getString("nombre"));
        producto.setCategoria(rs.getString("categoria"));
        producto.setPrecio(rs.getDouble("precio"));
        producto.setDisponible(rs.getBoolean("disponible"));
        producto.setFechaRegistro(rs.getTimestamp("fecha_registro").toLocalDateTime());
        return producto;
    }
}
