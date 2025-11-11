package com.restaurante.util;

import com.restaurante.config.SupabaseConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestor de conexiones a la base de datos.
 * Implementa el patrón Singleton para reutilizar conexiones.
 */
public class ConexionDB {
    
    private static Connection conexion = null;
    
    /**
     * Constructor privado para evitar instanciación.
     */
    private ConexionDB() {
    }
    
    /**
     * Obtiene una conexión activa a la base de datos.
     * Si no existe conexión o está cerrada, crea una nueva.
     * 
     * @return conexión activa a la base de datos
     * @throws SQLException si hay error al conectar
     */
    public static Connection obtenerConexion() throws SQLException {
        // Verificar si la configuración está completa
        if (!SupabaseConfig.isConfigured()) {
            throw new SQLException(
                "ERROR: La configuración de Supabase no está completa.\n" +
                "Por favor, edita el archivo SupabaseConfig.java con tus credenciales reales."
            );
        }
        
        // Si no hay conexión o está cerrada, crear una nueva
        if (conexion == null || conexion.isClosed()) {
            try {
                // Cargar el driver de PostgreSQL
                Class.forName("org.postgresql.Driver");
                
                // Crear la conexión
                String url = SupabaseConfig.getJdbcUrl();
                String user = SupabaseConfig.getDbUser();
                String password = SupabaseConfig.getDbPassword();
                
                conexion = DriverManager.getConnection(url, user, password);
                
                System.out.println("✓ Conexión a Supabase establecida correctamente");
                
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver de PostgreSQL no encontrado: " + e.getMessage());
            } catch (SQLException e) {
                throw new SQLException("Error al conectar con Supabase: " + e.getMessage());
            }
        }
        
        return conexion;
    }
    
    /**
     * Cierra la conexión activa si existe.
     */
    public static void cerrarConexion() {
        if (conexion != null) {
            try {
                if (!conexion.isClosed()) {
                    conexion.close();
                    System.out.println("✓ Conexión cerrada");
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
    
    /**
     * Verifica si hay una conexión activa.
     * 
     * @return true si hay conexión activa, false en caso contrario
     */
    public static boolean hayConexion() {
        try {
            return conexion != null && !conexion.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
