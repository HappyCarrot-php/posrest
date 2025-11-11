package com.restaurante.model;

/**
 * Clase de modelo para representar una mesa del restaurante.
 */
public class Mesa {
    
    private int id;
    private int numero;
    private String estado;
    
    // Constructor vacío
    public Mesa() {
    }
    
    // Constructor completo
    public Mesa(int id, int numero, String estado) {
        this.id = id;
        this.numero = numero;
        this.estado = estado;
    }
    
    // Constructor sin ID (para inserción)
    public Mesa(int numero, String estado) {
        this.numero = numero;
        this.estado = estado;
    }
    
    // Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getNumero() {
        return numero;
    }
    
    public void setNumero(int numero) {
        this.numero = numero;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    @Override
    public String toString() {
        return "Mesa " + numero + " (" + estado + ")";
    }
}
