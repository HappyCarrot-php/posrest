package com.restaurante.config;

/**
 * Configuración centralizada para la conexión a Supabase.
 * 
 * IMPORTANTE: Reemplaza estos valores con tus credenciales reales de Supabase:
 * 1. Ve a tu proyecto en https://app.supabase.com
 * 2. Settings > API
 * 3. Copia tu Project URL y anon/public key
 */
public class SupabaseConfig {
    
    /**
     * URL del proyecto de Supabase.
     * Formato: https://xxxxxxxxxxxx.supabase.co
     */
    public static final String SUPABASE_URL = "https://aneomlnyijkawaksrakq.supabase.co";
    
    /**
     * Clave anónima (anon key) de Supabase.
     * Esta clave es segura para uso público.
     */
    public static final String SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImFuZW9tbG55aWprYXdha3NyYWtxIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjI4MjcxODQsImV4cCI6MjA3ODQwMzE4NH0.5BQ15QMBtWCPT4aTCvEAlhj8SpqyRG5bvNt1KyljBBs";
    
    /**
     * Obtiene la URL de conexión JDBC para PostgreSQL.
     * Supabase usa PostgreSQL en el puerto 5432.
     * 
     * @return URL de conexión JDBC completa
     */
    public static String getJdbcUrl() {
        // Extraer el ID del proyecto de la URL
        String projectId = SUPABASE_URL.replace("https://", "").replace(".supabase.co", "");
        
        // Construir URL de conexión PostgreSQL
        // Formato: jdbc:postgresql://db.PROJECT_ID.supabase.co:5432/postgres
        return "jdbc:postgresql://db." + projectId + ".supabase.co:5432/postgres";
    }
    
    /**
     * Obtiene el usuario de la base de datos.
     * Por defecto Supabase usa 'postgres'.
     * 
     * @return nombre de usuario
     */
    public static String getDbUser() {
        return "postgres";
    }
    
    /**
     * Obtiene la contraseña de la base de datos.
     * IMPORTANTE: Reemplaza esto con tu contraseña real de Supabase.
     * La encuentras en Settings > Database > Connection string
     * 
     * @return contraseña de la base de datos
     */
    public static String getDbPassword() {
        // TODO: Reemplaza "TU-PASSWORD-AQUI" con tu contraseña real de Supabase
        return "Kiki2006?";
    }
    
    /**
     * Verifica si la configuración ha sido establecida correctamente.
     * 
     * @return true si la configuración está lista, false si aún tiene valores por defecto
     */
    public static boolean isConfigured() {
        return !SUPABASE_URL.contains("TU-PROJECT") && 
               !SUPABASE_ANON_KEY.contains("TU-ANON") &&
               !getDbPassword().contains("TU-PASSWORD");
    }
}
