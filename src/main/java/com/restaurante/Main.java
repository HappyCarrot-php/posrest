package com.restaurante;

import com.restaurante.config.SupabaseConfig;
import com.restaurante.util.ConexionDB;
import com.restaurante.view.LoginFrame;
import javax.swing.*;

/**
 * Clase principal del Sistema POS para Restaurante.
 * Punto de entrada de la aplicación.
 * admin@restaurante.com', 'admin123', 'administrador'
   Juan Pérez', 'cajero@restaurante.com', 'cajero123', 'cajero'
   María González', 'mesero@restaurante.com', 'mesero123', 'mesero'
 */
public class Main {
    
    public static void main(String[] args) {
        // Configurar Look and Feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo establecer el Look and Feel del sistema");
        }
        
        // Verificar configuración de Supabase
        if (!SupabaseConfig.isConfigured()) {
            mostrarAdvertenciaConfiguracion();
        }
        
        // Verificar conexión a base de datos
        verificarConexion();
        
        // Iniciar aplicación
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
    
    /**
     * Muestra una advertencia si la configuración no está completa.
     */
    private static void mostrarAdvertenciaConfiguracion() {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(
                null,
                "⚠️ ATENCIÓN: La configuración de Supabase no está completa.\n\n" +
                "Por favor, edite el archivo:\n" +
                "src/main/java/com/restaurante/config/SupabaseConfig.java\n\n" +
                "Y configure:\n" +
                "1. SUPABASE_URL\n" +
                "2. SUPABASE_ANON_KEY\n" +
                "3. Contraseña de la base de datos (en getDbPassword)\n\n" +
                "Consulte el archivo README.md para más información.",
                "Configuración Pendiente",
                JOptionPane.WARNING_MESSAGE
            );
        });
    }
    
    /**
     * Verifica la conexión a la base de datos al iniciar.
     */
    private static void verificarConexion() {
        try {
            ConexionDB.obtenerConexion();
            System.out.println("✓ Sistema iniciado correctamente");
            System.out.println("✓ Conexión a base de datos verificada");
            System.out.println("\n===========================================");
            System.out.println("  SISTEMA POS PARA RESTAURANTE");
            System.out.println("  Versión 1.0.0");
            System.out.println("===========================================\n");
        } catch (Exception e) {
            System.err.println("✗ Error al conectar con la base de datos:");
            System.err.println(e.getMessage());
            System.err.println("\nVerifique su configuración en SupabaseConfig.java");
            
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(
                    null,
                    "No se pudo conectar a la base de datos.\n\n" +
                    "Error: " + e.getMessage() + "\n\n" +
                    "Verifique:\n" +
                    "1. Que la configuración en SupabaseConfig.java sea correcta\n" +
                    "2. Que su conexión a internet esté activa\n" +
                    "3. Que haya ejecutado el script CREATE_DB.sql en Supabase\n\n" +
                    "El sistema se cerrará.",
                    "Error de Conexión",
                    JOptionPane.ERROR_MESSAGE
                );
                System.exit(1);
            });
        }
    }
}
