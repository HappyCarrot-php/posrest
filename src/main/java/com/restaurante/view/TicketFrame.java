package com.restaurante.view;

import com.restaurante.controller.VentaController;
import com.restaurante.model.Ticket;
import com.restaurante.model.Usuario;
import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Ventana para mostrar el ticket de venta.
 */
public class TicketFrame extends JFrame {
    
    private Ticket ticket;
    private List<VentaController.ItemVenta> items;
    private Usuario usuario;
    
    public TicketFrame(Ticket ticket, List<VentaController.ItemVenta> items, Usuario usuario) {
        this.ticket = ticket;
        this.items = items;
        this.usuario = usuario;
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        setTitle("Ticket - " + ticket.getFolio());
        setSize(400, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Área de texto para el ticket
        JTextArea txtTicket = new JTextArea();
        txtTicket.setEditable(false);
        txtTicket.setFont(new Font("Courier New", Font.PLAIN, 12));
        txtTicket.setText(generarTicketTexto());
        
        JScrollPane scrollTicket = new JScrollPane(txtTicket);
        
        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnImprimir = new JButton("Imprimir");
        JButton btnCerrar = new JButton("Cerrar");
        
        btnImprimir.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "Función de impresión simulada\n(Conecte una impresora de tickets para usar esta función)", 
                "Imprimir", JOptionPane.INFORMATION_MESSAGE);
        });
        
        btnCerrar.addActionListener(e -> dispose());
        
        panelBotones.add(btnImprimir);
        panelBotones.add(btnCerrar);
        
        panelPrincipal.add(scrollTicket, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
        add(panelPrincipal);
    }
    
    private String generarTicketTexto() {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        
        sb.append("========================================\n");
        sb.append("         SISTEMA POS RESTAURANTE        \n");
        sb.append("========================================\n\n");
        
        sb.append("Folio: ").append(ticket.getFolio()).append("\n");
        sb.append("Fecha: ").append(ticket.getFechaEmision().format(formatter)).append("\n");
        sb.append("Atendió: ").append(usuario.getNombre()).append("\n");
        sb.append("----------------------------------------\n\n");
        
        sb.append("PRODUCTOS:\n\n");
        sb.append(String.format("%-20s %5s %10s\n", "Producto", "Cant.", "Subtotal"));
        sb.append("----------------------------------------\n");
        
        for (VentaController.ItemVenta item : items) {
            String nombre = item.getProducto().getNombre();
            if (nombre.length() > 20) {
                nombre = nombre.substring(0, 17) + "...";
            }
            sb.append(String.format("%-20s %5d $%9.2f\n", 
                nombre, 
                item.getCantidad(), 
                item.getSubtotal()));
        }
        
        sb.append("----------------------------------------\n");
        sb.append(String.format("%-26s $%9.2f\n", "TOTAL:", ticket.getTotal()));
        sb.append(String.format("%-26s $%9.2f\n", "PAGADO:", ticket.getTotal() + ticket.getCambio()));
        sb.append(String.format("%-26s $%9.2f\n", "CAMBIO:", ticket.getCambio()));
        sb.append("========================================\n\n");
        
        sb.append("       ¡GRACIAS POR SU PREFERENCIA!     \n");
        sb.append("           www.restaurante.com          \n");
        sb.append("========================================\n");
        
        return sb.toString();
    }
}
