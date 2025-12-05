package com.restaurante.util;

import com.restaurante.controller.VentaController;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utilidades financieras para cálculos de ventas e importes.
 */
public final class FinancialUtils {

    private static final BigDecimal IVA_RATE = new BigDecimal("0.16");

    private FinancialUtils() {
        // Utility class
    }

    /**
     * Calcula subtotal, IVA y total a partir de los ítems de una venta.
     *
     * @param items lista de ítems vendidos
     * @return contenedor con subtotal, IVA y total redondeados a dos decimales
     */
    public static TotalesVenta calcularTotales(List<VentaController.ItemVenta> items) {
        BigDecimal subtotal = BigDecimal.ZERO;

        if (items != null) {
            for (VentaController.ItemVenta item : items) {
                if (item != null) {
                    subtotal = subtotal.add(BigDecimal.valueOf(item.getSubtotal()));
                }
            }
        }

        subtotal = subtotal.setScale(2, RoundingMode.HALF_UP);
        BigDecimal iva = subtotal.multiply(IVA_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(iva).setScale(2, RoundingMode.HALF_UP);

        return new TotalesVenta(subtotal.doubleValue(), iva.doubleValue(), total.doubleValue());
    }

    /**
     * Devuelve una copia inmutable de la lista de ítems, segura para exportaciones.
     *
     * @param items lista original
     * @return lista copiada y no modificable
     */
    public static List<VentaController.ItemVenta> clonarItems(List<VentaController.ItemVenta> items) {
        if (items == null || items.isEmpty()) {
            return Collections.emptyList();
        }
        List<VentaController.ItemVenta> copia = new ArrayList<>(items.size());
        for (VentaController.ItemVenta item : items) {
            if (item != null) {
                copia.add(new VentaController.ItemVenta(item.getProducto(), item.getCantidad()));
            }
        }
        return copia;
    }

    /**
     * Redondea un valor monetario a dos decimales usando HALF_UP.
     */
    public static double redondear(double valor) {
        return BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * Contenedor inmutable de totales de una venta.
     */
    public static final class TotalesVenta {
        private final double subtotal;
        private final double iva;
        private final double total;

        public TotalesVenta(double subtotal, double iva, double total) {
            this.subtotal = subtotal;
            this.iva = iva;
            this.total = total;
        }

        public double getSubtotal() {
            return subtotal;
        }

        public double getIva() {
            return iva;
        }

        public double getTotal() {
            return total;
        }
    }
}
