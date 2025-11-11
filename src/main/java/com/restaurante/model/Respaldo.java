package com.restaurante.model;

import java.time.LocalDateTime;

/**
 * Clase de modelo para representar un registro de auditoría.
 */
public class Respaldo {
    
    private int id;
    private String tipoOperacion;
    private String descripcion;
    private LocalDateTime fecha;
    
    // Constructor vacío
    public Respaldo() {
    }
    
    // Constructor completo
    public Respaldo(int id, String tipoOperacion, String descripcion, LocalDateTime fecha) {
        this.id = id;
        this.tipoOperacion = tipoOperacion;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }
    
    // Constructor sin ID (para inserción)
    public Respaldo(String tipoOperacion, String descripcion) {
        this.tipoOperacion = tipoOperacion;
        this.descripcion = descripcion;
    }
    
    // Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTipoOperacion() {
        return tipoOperacion;
    }
    
    public void setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public LocalDateTime getFecha() {
        return fecha;
    }
    
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    
    @Override
    public String toString() {
        return tipoOperacion + " - " + descripcion;
    }
}
