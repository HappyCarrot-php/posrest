package com.restaurante.controller;

import com.restaurante.dao.UsuarioDAO;
import com.restaurante.dao.RespaldoDAO;
import com.restaurante.model.Usuario;
import com.restaurante.util.Validaciones;
import java.util.List;

/**
 * Controlador para gestionar la lógica de negocio de usuarios.
 */
public class UsuarioController {
    
    private final UsuarioDAO usuarioDAO;
    private final RespaldoDAO respaldoDAO;
    private Usuario usuarioActual; // Usuario logueado actualmente
    
    public UsuarioController() {
        this.usuarioDAO = new UsuarioDAO();
        this.respaldoDAO = new RespaldoDAO();
        this.usuarioActual = null;
    }
    
    /**
     * Valida las credenciales de un usuario (login).
     * 
     * @param correo correo del usuario
     * @param contraseña contraseña del usuario
     * @return usuario si las credenciales son válidas, null en caso contrario
     */
    public Usuario login(String correo, String contraseña) {
        // Validar datos de entrada
        if (!Validaciones.esCorreoValido(correo)) {
            System.err.println("Correo inválido");
            return null;
        }
        
        if (!Validaciones.noEsVacio(contraseña)) {
            System.err.println("Contraseña vacía");
            return null;
        }
        
        // Validar credenciales
        Usuario usuario = usuarioDAO.validarCredenciales(correo, contraseña);
        
        if (usuario != null) {
            this.usuarioActual = usuario;
            respaldoDAO.registrarOperacion("LOGIN", 
                "Usuario: " + usuario.getNombre() + " (" + usuario.getRol() + ") inició sesión");
        }
        
        return usuario;
    }
    
    /**
     * Cierra la sesión del usuario actual.
     */
    public void logout() {
        if (usuarioActual != null) {
            respaldoDAO.registrarOperacion("LOGOUT", 
                "Usuario: " + usuarioActual.getNombre() + " cerró sesión");
            usuarioActual = null;
        }
    }
    
    /**
     * Obtiene el usuario actualmente logueado.
     * 
     * @return usuario actual, o null si no hay sesión activa
     */
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
    
    /**
     * Verifica si hay un usuario logueado.
     * 
     * @return true si hay sesión activa, false en caso contrario
     */
    public boolean haySesionActiva() {
        return usuarioActual != null;
    }
    
    /**
     * Crea un nuevo usuario.
     * 
     * @param nombre nombre del usuario
     * @param correo correo del usuario
     * @param contraseña contraseña del usuario
     * @param rol rol del usuario
     * @return true si se creó correctamente, false en caso contrario
     */
    public boolean crearUsuario(String nombre, String correo, String contraseña, String rol) {
        // Validaciones
        if (!Validaciones.soloLetras(nombre)) {
            System.err.println("Nombre inválido");
            return false;
        }
        
        if (!Validaciones.esCorreoValido(correo)) {
            System.err.println("Correo inválido");
            return false;
        }
        
        if (!Validaciones.longitudMinima(contraseña, 6)) {
            System.err.println("Contraseña debe tener al menos 6 caracteres");
            return false;
        }
        
        if (!Validaciones.esRolValido(rol)) {
            System.err.println("Rol inválido");
            return false;
        }
        
        // Verificar si el correo ya existe
        if (usuarioDAO.buscarPorCorreo(correo) != null) {
            System.err.println("El correo ya está registrado");
            return false;
        }
        
        // Crear usuario
        Usuario usuario = new Usuario(nombre, correo, contraseña, rol.toLowerCase());
        boolean resultado = usuarioDAO.insertar(usuario);
        
        if (resultado) {
            respaldoDAO.registrarOperacion("CREAR_USUARIO", 
                "Nuevo usuario creado: " + nombre + " (" + rol + ")");
        }
        
        return resultado;
    }
    
    /**
     * Actualiza un usuario existente.
     * 
     * @param usuario usuario con datos actualizados
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizarUsuario(Usuario usuario) {
        // Validaciones
        if (!Validaciones.soloLetras(usuario.getNombre())) {
            System.err.println("Nombre inválido");
            return false;
        }
        
        if (!Validaciones.esCorreoValido(usuario.getCorreo())) {
            System.err.println("Correo inválido");
            return false;
        }
        
        if (!Validaciones.esRolValido(usuario.getRol())) {
            System.err.println("Rol inválido");
            return false;
        }
        
        boolean resultado = usuarioDAO.actualizar(usuario);
        
        if (resultado) {
            respaldoDAO.registrarOperacion("ACTUALIZAR_USUARIO", 
                "Usuario actualizado: " + usuario.getNombre() + " (ID: " + usuario.getId() + ")");
        }
        
        return resultado;
    }
    
    /**
     * Elimina un usuario por su ID.
     * 
     * @param id ID del usuario a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminarUsuario(int id) {
        Usuario usuario = usuarioDAO.buscarPorId(id);
        
        if (usuario == null) {
            System.err.println("Usuario no encontrado");
            return false;
        }
        
        boolean resultado = usuarioDAO.eliminar(id);
        
        if (resultado) {
            respaldoDAO.registrarOperacion("ELIMINAR_USUARIO", 
                "Usuario eliminado: " + usuario.getNombre() + " (ID: " + id + ")");
        }
        
        return resultado;
    }
    
    /**
     * Obtiene todos los usuarios.
     * 
     * @return lista de todos los usuarios
     */
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioDAO.obtenerTodos();
    }
    
    /**
     * Busca un usuario por su ID.
     * 
     * @param id ID del usuario
     * @return usuario encontrado, o null si no existe
     */
    public Usuario buscarUsuarioPorId(int id) {
        return usuarioDAO.buscarPorId(id);
    }
    
    /**
     * Verifica si el usuario actual tiene permisos de administrador.
     * 
     * @return true si es administrador, false en caso contrario
     */
    public boolean esAdministrador() {
        return usuarioActual != null && 
               usuarioActual.getRol().equalsIgnoreCase("administrador");
    }
    
    /**
     * Verifica si el usuario actual tiene permisos de cajero.
     * 
     * @return true si es cajero, false en caso contrario
     */
    public boolean esCajero() {
        return usuarioActual != null && 
               usuarioActual.getRol().equalsIgnoreCase("cajero");
    }
    
    /**
     * Verifica si el usuario actual tiene permisos de mesero.
     * 
     * @return true si es mesero, false en caso contrario
     */
    public boolean esMesero() {
        return usuarioActual != null && 
               usuarioActual.getRol().equalsIgnoreCase("mesero");
    }
}
