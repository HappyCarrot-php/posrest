package com.restaurante.model;

import java.time.LocalDateTime;

/**
 * Clase de modelo para representar una venta.
 */
public class Venta {
    
    private int id;
    private int idUsuario;
    private Integer idMesa; // Puede ser null
    private double total;
    private LocalDateTime fechaVenta;
    
    // Constructor vacío
    public Venta() {
    }
    
    // Constructor completo
    public Venta(int id, int idUsuario, Integer idMesa, double total, 
                 LocalDateTime fechaVenta) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idMesa = idMesa;
        this.total = total;
        this.fechaVenta = fechaVenta;
    }
    
    // Constructor sin ID (para inserción)
    public Venta(int idUsuario, Integer idMesa, double total) {
        this.idUsuario = idUsuario;
        this.idMesa = idMesa;
        this.total = total;
    }
    
    // Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getIdUsuario() {
        return idUsuario;
    }
    
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    public Integer getIdMesa() {
        return idMesa;
    }
    
    public void setIdMesa(Integer idMesa) {
        this.idMesa = idMesa;
    }
    
    public double getTotal() {
        return total;
    }
    
    public void setTotal(double total) {
        this.total = total;
    }
    
    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }
    
    public void setFechaVenta(LocalDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }
    
    @Override
    public String toString() {
        return "Venta #" + id + " - Total: $" + total;
    }
}
