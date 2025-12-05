package com.restaurante.view;

import com.restaurante.controller.VentaController;
import com.restaurante.model.Ticket;
import com.restaurante.model.Usuario;
import com.restaurante.util.FinancialUtils;
import com.restaurante.util.FinancialUtils.TotalesVenta;
import com.restaurante.util.TicketPdfGenerator;
import com.lowagie.text.DocumentException;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.*;

/**
 * Ventana para mostrar el ticket de venta.
 */
public class TicketFrame extends JFrame {
    
    private final Ticket ticket;
    private final List<VentaController.ItemVenta> items;
    private final Usuario usuario;
    
    public TicketFrame(Ticket ticket, List<VentaController.ItemVenta> items, Usuario usuario) {
        this.ticket = ticket;
        this.items = new ArrayList<>(FinancialUtils.clonarItems(items));
        this.usuario = usuario;
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        setTitle("Ticket - " + ticket.getFolio());
        setSize(400, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 16));
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Área de texto para el ticket
        JEditorPane ticketPane = new JEditorPane();
        ticketPane.setEditable(false);
        ticketPane.setContentType("text/html");
        ticketPane.setText(generarTicketHtml());
        ticketPane.setCaretPosition(0);
        ticketPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        ticketPane.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        
        JScrollPane scrollTicket = new JScrollPane(ticketPane);
        scrollTicket.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));
        
        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        panelBotones.setOpaque(false);
        JButton btnImprimir = crearBotonAccion("Imprimir", new Color(55, 125, 255));
        JButton btnPdf = crearBotonAccion("Exportar PDF", new Color(37, 168, 79));
        JButton btnCerrar = crearBotonAccion("Cerrar", new Color(108, 117, 125));
        
        btnImprimir.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "Función de impresión simulada\n(Conecte una impresora de tickets para usar esta función)", 
                "Imprimir", JOptionPane.INFORMATION_MESSAGE);
        });
        
        btnPdf.addActionListener(e -> exportarTicketPdf());
        btnCerrar.addActionListener(e -> dispose());
        
        panelBotones.add(btnImprimir);
        panelBotones.add(btnPdf);
        panelBotones.add(btnCerrar);
        
        panelPrincipal.add(scrollTicket, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
        add(panelPrincipal);
    }

    private void exportarTicketPdf() {
        try {
            Path archivo = TicketPdfGenerator.generarYMostrar(ticket, usuario, items);
            JOptionPane.showMessageDialog(
                this,
                "Ticket exportado correctamente:\n" + archivo,
                "PDF generado",
                JOptionPane.INFORMATION_MESSAGE
            );
        } catch (IOException | DocumentException ex) {
            JOptionPane.showMessageDialog(
                this,
                "No se pudo generar el ticket en PDF: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private String generarTicketHtml() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        TotalesVenta totales = FinancialUtils.calcularTotales(items);

        double totalRegistrado = FinancialUtils.redondear(ticket.getTotal());
        double subtotal = totales.getSubtotal();
        double iva = FinancialUtils.redondear(totalRegistrado - subtotal);

        if (iva < 0) {
            iva = totales.getIva();
            subtotal = FinancialUtils.redondear(totalRegistrado - iva);
        }

        double pagado = FinancialUtils.redondear(totalRegistrado + ticket.getCambio());
        double cambio = FinancialUtils.redondear(ticket.getCambio());

        StringBuilder html = new StringBuilder();
        html.append("<html><body style='font-family:Segoe UI, sans-serif; background-color:#f7f8fa; margin:0;'>");
        html.append("<div style='max-width:320px;margin:0 auto;background:#ffffff;border:1px solid #e5e7eb;border-radius:12px;box-shadow:0 6px 18px rgba(0,0,0,0.08);padding:24px;'>");
        html.append("<h2 style='margin:0;text-align:center;color:#0f172a;font-size:20px;letter-spacing:1px;'>Sistema POS Restaurante</h2>");
        html.append("<p style='text-align:center;color:#64748b;margin:4px 0 18px 0;font-size:11px;'>Su recibo electrónico</p>");

        html.append("<div style='background:#f1f5f9;border-radius:10px;padding:12px 16px;margin-bottom:18px;'>");
        html.append(String.format(Locale.US, "<div style='display:flex;justify-content:space-between;font-size:12px;color:#475569;'><span>Folio</span><span>%s</span></div>", ticket.getFolio()));
        if (ticket.getFechaEmision() != null) {
            html.append(String.format(Locale.US, "<div style='display:flex;justify-content:space-between;font-size:12px;color:#475569;'><span>Fecha</span><span>%s</span></div>", ticket.getFechaEmision().format(formatter)));
        }
        if (usuario != null) {
            html.append(String.format(Locale.US, "<div style='display:flex;justify-content:space-between;font-size:12px;color:#475569;'><span>Atendió</span><span>%s</span></div>", usuario.getNombre()));
        }
        html.append("</div>");

        html.append("<table style='width:100%;border-collapse:collapse;margin-bottom:18px;font-size:12px;'>");
        html.append("<thead><tr style='background:#0f172a;color:#fff;'>");
        html.append("<th style='text-align:left;padding:8px;border-top-left-radius:8px;'>Producto</th>");
        html.append("<th style='text-align:center;padding:8px;'>Cant.</th>");
        html.append("<th style='text-align:right;padding:8px;border-top-right-radius:8px;'>Importe</th>");
        html.append("</tr></thead><tbody>");

        for (VentaController.ItemVenta item : items) {
            html.append("<tr style='border-bottom:1px solid #e2e8f0;'>");
            html.append(String.format(Locale.US, "<td style='padding:8px;color:#1e293b;'>%s</td>", item.getProducto().getNombre()));
            html.append(String.format(Locale.US, "<td style='padding:8px;text-align:center;color:#1e293b;'>%d</td>", item.getCantidad()));
            html.append(String.format(Locale.US, "<td style='padding:8px;text-align:right;color:#1e293b;'>%s</td>", formatearMoneda(item.getSubtotal())));
            html.append("</tr>");
        }

        html.append("</tbody></table>");

        html.append("<div style='background:#f8fafc;border-radius:10px;padding:16px;font-size:13px;margin-bottom:18px;'>");
        html.append(String.format(Locale.US, "<div style='display:flex;justify-content:space-between;margin-bottom:6px;color:#475569;'><span>Subtotal</span><span>%s</span></div>", formatearMoneda(subtotal)));
        html.append(String.format(Locale.US, "<div style='display:flex;justify-content:space-between;margin-bottom:8px;color:#475569;'><span>IVA (16%%)</span><span>%s</span></div>", formatearMoneda(iva)));
        html.append(String.format(Locale.US, "<div style='display:flex;justify-content:space-between;font-weight:600;color:#0f172a;font-size:15px;'><span>Total</span><span>%s</span></div>", formatearMoneda(totalRegistrado)));
        html.append("<hr style='border:0;border-top:1px dashed #cbd5f5;margin:12px 0;'>");
        html.append(String.format(Locale.US, "<div style='display:flex;justify-content:space-between;margin-bottom:4px;color:#475569;'><span>Pagado</span><span>%s</span></div>", formatearMoneda(pagado)));
        html.append(String.format(Locale.US, "<div style='display:flex;justify-content:space-between;color:%s;font-weight:600;'><span>Cambio</span><span>%s</span></div>", cambio < 0 ? "#dc2626" : "#047857", formatearMoneda(cambio)));
        html.append("</div>");

        html.append("<p style='text-align:center;color:#475569;font-size:11px;margin:0;'>¡Gracias por su preferencia!<br>www.restaurante.com</p>");
        html.append("</div></body></html>");

        return html.toString();
    }

    private String formatearMoneda(double valor) {
        return String.format(Locale.US, "$%,.2f", valor);
    }
    
    private JButton crearBotonAccion(String texto, Color baseColor) {
        return new BotonAccionTicket(texto, baseColor);
    }

    private static class BotonAccionTicket extends JButton {
        private final Color baseColor;
        private final Color hoverColor;
        private final Color pressedColor;

        BotonAccionTicket(String texto, Color baseColor) {
            super(texto);
            this.baseColor = baseColor;
            this.hoverColor = baseColor.brighter();
            this.pressedColor = baseColor.darker();

            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setOpaque(false);
            setRolloverEnabled(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            ButtonModel model = getModel();
            Color fill = baseColor;
            if (!model.isEnabled()) {
                fill = baseColor.darker();
            } else if (model.isPressed()) {
                fill = pressedColor;
            } else if (model.isRollover()) {
                fill = hoverColor;
            }

            g2.setColor(new Color(0, 0, 0, 35));
            g2.fillRoundRect(3, getHeight() - 8, getWidth() - 6, 8, 10, 10);

            g2.setPaint(new GradientPaint(0, 0, fill, 0, getHeight(), fill.darker()));
            g2.fillRoundRect(0, 0, getWidth(), getHeight() - 3, 12, 12);

            g2.setColor(new Color(255, 255, 255, 80));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 4, 12, 12);

            g2.dispose();
            super.paintComponent(g);
        }
    }
}
