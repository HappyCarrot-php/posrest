package com.restaurante.util;

import java.util.regex.Pattern;

/**
 * Clase de utilidades para validaciones de datos.
 */
public class Validaciones {
    
    // Expresión regular para validar correos electrónicos
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    /**
     * Valida que una cadena no esté vacía o nula.
     * 
     * @param valor cadena a validar
     * @return true si es válida, false si es nula o vacía
     */
    public static boolean noEsVacio(String valor) {
        return valor != null && !valor.trim().isEmpty();
    }
    
    /**
     * Valida formato de correo electrónico.
     * 
     * @param correo correo a validar
     * @return true si el formato es válido, false en caso contrario
     */
    public static boolean esCorreoValido(String correo) {
        if (!noEsVacio(correo)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(correo).matches();
    }
    
    /**
     * Valida que un número sea positivo.
     * 
     * @param numero número a validar
     * @return true si es mayor a cero, false en caso contrario
     */
    public static boolean esPositivo(double numero) {
        return numero > 0;
    }
    
    /**
     * Valida que un número entero sea positivo.
     * 
     * @param numero número a validar
     * @return true si es mayor a cero, false en caso contrario
     */
    public static boolean esPositivo(int numero) {
        return numero > 0;
    }
    
    /**
     * Valida que un valor numérico no sea negativo.
     * 
     * @param numero número a validar
     * @return true si es mayor o igual a cero, false en caso contrario
     */
    public static boolean noEsNegativo(double numero) {
        return numero >= 0;
    }
    
    /**
     * Valida longitud mínima de una cadena.
     * 
     * @param valor cadena a validar
     * @param longitudMinima longitud mínima requerida
     * @return true si cumple la longitud mínima, false en caso contrario
     */
    public static boolean longitudMinima(String valor, int longitudMinima) {
        return noEsVacio(valor) && valor.trim().length() >= longitudMinima;
    }
    
    /**
     * Valida que un texto contenga solo letras y espacios.
     * 
     * @param texto texto a validar
     * @return true si solo contiene letras y espacios, false en caso contrario
     */
    public static boolean soloLetras(String texto) {
        if (!noEsVacio(texto)) {
            return false;
        }
        return texto.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$");
    }
    
    /**
     * Valida que un rol sea válido.
     * 
     * @param rol rol a validar
     * @return true si es administrador, cajero o mesero
     */
    public static boolean esRolValido(String rol) {
        if (!noEsVacio(rol)) {
            return false;
        }
        String rolLower = rol.toLowerCase();
        return rolLower.equals("administrador") || 
               rolLower.equals("cajero") || 
               rolLower.equals("mesero");
    }
    
    /**
     * Valida que un estado de mesa sea válido.
     * 
     * @param estado estado a validar
     * @return true si es libre, ocupada o reservada
     */
    public static boolean esEstadoMesaValido(String estado) {
        if (!noEsVacio(estado)) {
            return false;
        }
        String estadoLower = estado.toLowerCase();
        return estadoLower.equals("libre") || 
               estadoLower.equals("ocupada") || 
               estadoLower.equals("reservada");
    }
    
    /**
     * Intenta convertir una cadena a double.
     * 
     * @param valor cadena a convertir
     * @return el valor convertido, o 0 si no es válido
     */
    public static double convertirADouble(String valor) {
        try {
            return Double.parseDouble(valor);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    /**
     * Intenta convertir una cadena a entero.
     * 
     * @param valor cadena a convertir
     * @return el valor convertido, o 0 si no es válido
     */
    public static int convertirAEntero(String valor) {
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
