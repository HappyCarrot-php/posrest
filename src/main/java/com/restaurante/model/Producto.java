package com.restaurante.model;

import java.time.LocalDateTime;

/**
 * Clase de modelo para representar un producto del menú.
 */
public class Producto {
    
    private int id;
    private String nombre;
    private String categoria;
    private double precio;
    private boolean disponible;
    private LocalDateTime fechaRegistro;
    
    // Constructor vacío
    public Producto() {
    }
    
    // Constructor completo
    public Producto(int id, String nombre, String categoria, double precio, 
                    boolean disponible, LocalDateTime fechaRegistro) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.disponible = disponible;
        this.fechaRegistro = fechaRegistro;
    }
    
    // Constructor sin ID (para inserción)
    public Producto(String nombre, String categoria, double precio, boolean disponible) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.disponible = disponible;
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
    
    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public double getPrecio() {
        return precio;
    }
    
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    
    public boolean isDisponible() {
        return disponible;
    }
    
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
    
    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }
    
    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
    @Override
    public String toString() {
        return nombre + " - $" + precio;
    }
}
