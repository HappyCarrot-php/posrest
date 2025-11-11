package com.restaurante.model;

import java.time.LocalDateTime;

/**
 * Clase de modelo para representar un ticket de venta.
 */
public class Ticket {
    
    private int id;
    private int idVenta;
    private String folio;
    private double total;
    private double cambio;
    private LocalDateTime fechaEmision;
    
    // Constructor vacío
    public Ticket() {
    }
    
    // Constructor completo
    public Ticket(int id, int idVenta, String folio, double total, 
                  double cambio, LocalDateTime fechaEmision) {
        this.id = id;
        this.idVenta = idVenta;
        this.folio = folio;
        this.total = total;
        this.cambio = cambio;
        this.fechaEmision = fechaEmision;
    }
    
    // Constructor sin ID (para inserción)
    public Ticket(int idVenta, String folio, double total, double cambio) {
        this.idVenta = idVenta;
        this.folio = folio;
        this.total = total;
        this.cambio = cambio;
    }
    
    // Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getIdVenta() {
        return idVenta;
    }
    
    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }
    
    public String getFolio() {
        return folio;
    }
    
    public void setFolio(String folio) {
        this.folio = folio;
    }
    
    public double getTotal() {
        return total;
    }
    
    public void setTotal(double total) {
        this.total = total;
    }
    
    public double getCambio() {
        return cambio;
    }
    
    public void setCambio(double cambio) {
        this.cambio = cambio;
    }
    
    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }
    
    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }
    
    @Override
    public String toString() {
        return "Ticket " + folio + " - Total: $" + total;
    }
}
