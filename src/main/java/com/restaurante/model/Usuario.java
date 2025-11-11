package com.restaurante.model;

import java.time.LocalDateTime;

/**
 * Clase de modelo para representar un usuario del sistema.
 */
public class Usuario {
    
    private int id;
    private String nombre;
    private String correo;
    private String contraseñaHash;
    private String rol;
    private LocalDateTime fechaCreacion;
    
    // Constructor vacío
    public Usuario() {
    }
    
    // Constructor completo
    public Usuario(int id, String nombre, String correo, String contraseñaHash, 
                   String rol, LocalDateTime fechaCreacion) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contraseñaHash = contraseñaHash;
        this.rol = rol;
        this.fechaCreacion = fechaCreacion;
    }
    
    // Constructor sin ID (para inserción)
    public Usuario(String nombre, String correo, String contraseñaHash, String rol) {
        this.nombre = nombre;
        this.correo = correo;
        this.contraseñaHash = contraseñaHash;
        this.rol = rol;
    }
    
    // Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getCorreo() {
        return correo;
    }
    
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    
    public String getContraseñaHash() {
        return contraseñaHash;
    }
    
    public void setContraseñaHash(String contraseñaHash) {
        this.contraseñaHash = contraseñaHash;
    }
    
    public String getRol() {
        return rol;
    }
    
    public void setRol(String rol) {
        this.rol = rol;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    @Override
    public String toString() {
        return nombre + " (" + rol + ")";
    }
}
