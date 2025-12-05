package com.restaurante.view;

import com.restaurante.controller.VentaController;
import com.restaurante.model.Venta;
import com.restaurante.util.JasperReportManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;

/**
 * Ventana de reportes de ventas con diseño moderno.
 */
public class ReporteFrame extends JFrame {
    
    private final VentaController ventaController;
    private JTable tablaVentas;
    private DefaultTableModel modeloTabla;
    private JComboBox<String> cboPeriodo;
    private JLabel lblTotalVentas;
    private JLabel lblCantidadVentas;
    private LocalDateTime ultimoInicio;
    private LocalDateTime ultimoFin;
    private boolean usarRangoFechas;
    
    // Colores modernos
    private static final Color PRIMARY_COLOR = new Color(74, 144, 226);
    private static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Color TEXT_PRIMARY = new Color(33, 37, 41);
    private static final Color TEXT_SECONDARY = new Color(108, 117, 125);
    
    public ReporteFrame() {
        this.ventaController = new VentaController();
        inicializarComponentes();
        cargarReporteHoy();
    }
    
    private void inicializarComponentes() {
        setTitle("Reportes de Ventas");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Header con título y botón regresar
        add(createHeader(), BorderLayout.NORTH);
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel de resumen con cards
        mainPanel.add(createSummaryPanel(), BorderLayout.NORTH);
        
        // Tabla de ventas
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(15, 25, 15, 25)
        ));
        
        // Título
        JLabel lblTitle = new JLabel("Reportes de Ventas");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(TEXT_PRIMARY);
        
        // Botón regresar
        ModernButton btnBack = new ModernButton("Regresar", TEXT_SECONDARY, Color.WHITE);
        btnBack.setPreferredSize(new Dimension(130, 35));
        btnBack.addActionListener(e -> dispose());
        
        header.add(lblTitle, BorderLayout.WEST);
        header.add(btnBack, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 0));
        panel.setOpaque(false);
        
        // Card 1: Filtro de período
        JPanel cardFiltro = createCard();
        cardFiltro.setLayout(new BorderLayout(10, 10));
        
        JLabel lblFiltro = new JLabel("Período");
        lblFiltro.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblFiltro.setForeground(TEXT_SECONDARY);
        
        cboPeriodo = new JComboBox<>(new String[]{"Hoy", "Esta Semana", "Este Mes", "Todas"});
        cboPeriodo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboPeriodo.addActionListener(e -> cargarReporte());
        
        ModernButton btnActualizar = new ModernButton("Actualizar", PRIMARY_COLOR, Color.WHITE);
        btnActualizar.setPreferredSize(new Dimension(120, 35));
        btnActualizar.addActionListener(e -> cargarReporte());

        ModernButton btnJasper = new ModernButton("Ver en Jasper", SUCCESS_COLOR, Color.WHITE);
        btnJasper.setPreferredSize(new Dimension(120, 35));
        btnJasper.addActionListener(e -> generarReporteJasper());
        
        JPanel filtroTop = new JPanel(new BorderLayout());
        filtroTop.setOpaque(false);
        filtroTop.add(lblFiltro, BorderLayout.NORTH);
        filtroTop.add(Box.createRigidArea(new Dimension(0, 8)), BorderLayout.CENTER);
        
        cardFiltro.add(filtroTop, BorderLayout.NORTH);
        cardFiltro.add(cboPeriodo, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        buttonsPanel.setOpaque(false);
        buttonsPanel.add(btnActualizar);
        buttonsPanel.add(btnJasper);

        cardFiltro.add(buttonsPanel, BorderLayout.SOUTH);
        
        // Card 2: Cantidad de ventas
        JPanel cardCantidad = createCard();
        cardCantidad.setLayout(new BorderLayout());
        
        JLabel lblTituloCant = new JLabel("Total de Ventas");
        lblTituloCant.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTituloCant.setForeground(TEXT_SECONDARY);
        
        lblCantidadVentas = new JLabel("0");
        lblCantidadVentas.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblCantidadVentas.setForeground(PRIMARY_COLOR);
        
        cardCantidad.add(lblTituloCant, BorderLayout.NORTH);
        cardCantidad.add(lblCantidadVentas, BorderLayout.CENTER);
        
        // Card 3: Monto total
        JPanel cardMonto = createCard();
        cardMonto.setLayout(new BorderLayout());
        
        JLabel lblTituloMonto = new JLabel("Monto Total");
        lblTituloMonto.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTituloMonto.setForeground(TEXT_SECONDARY);
        
        lblTotalVentas = new JLabel("$0.00");
        lblTotalVentas.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTotalVentas.setForeground(SUCCESS_COLOR);
        
        cardMonto.add(lblTituloMonto, BorderLayout.NORTH);
        cardMonto.add(lblTotalVentas, BorderLayout.CENTER);
        
        panel.add(cardFiltro);
        panel.add(cardCantidad);
        panel.add(cardMonto);
        
        return panel;
    }
    
    private JPanel createCard() {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        return card;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);
        
        // Título
        JLabel lblTitulo = new JLabel("Detalle de Ventas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(TEXT_PRIMARY);
        
        // Tabla
        String[] columnas = {"ID", "Fecha", "Total", "Usuario ID", "Mesa ID"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaVentas = new JTable(modeloTabla);
        tablaVentas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaVentas.setRowHeight(35);
        tablaVentas.setSelectionBackground(new Color(74, 144, 226, 50));
        tablaVentas.setSelectionForeground(TEXT_PRIMARY);
        tablaVentas.setShowGrid(false);
        tablaVentas.setIntercellSpacing(new Dimension(0, 0));
        
        // Header de tabla
        tablaVentas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaVentas.getTableHeader().setBackground(BACKGROUND_COLOR);
        tablaVentas.getTableHeader().setForeground(TEXT_PRIMARY);
        tablaVentas.getTableHeader().setPreferredSize(new Dimension(0, 40));
        
        // Ancho de columnas
        tablaVentas.getColumnModel().getColumn(0).setPreferredWidth(60);
        tablaVentas.getColumnModel().getColumn(1).setPreferredWidth(200);
        tablaVentas.getColumnModel().getColumn(2).setPreferredWidth(120);
        tablaVentas.getColumnModel().getColumn(3).setPreferredWidth(100);
        tablaVentas.getColumnModel().getColumn(4).setPreferredWidth(100);
        
        JScrollPane scrollPane = new JScrollPane(tablaVentas);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        scrollPane.setBackground(Color.WHITE);
        
        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void cargarReporte() {
        String periodo = (String) cboPeriodo.getSelectedItem();
        
        switch (periodo) {
            case "Hoy":
                cargarReporteHoy();
                break;
            case "Esta Semana":
                cargarReporteSemana();
                break;
            case "Este Mes":
                cargarReporteMes();
                break;
            case "Todas":
                cargarReporteTodas();
                break;
        }
    }

    private void generarReporteJasper() {
        Map<String, Object> parametros = new HashMap<>();

        if (usarRangoFechas && ultimoInicio != null && ultimoFin != null) {
            parametros.put("FECHA_INICIO", Timestamp.valueOf(ultimoInicio));
            parametros.put("FECHA_FIN", Timestamp.valueOf(ultimoFin));
        } else {
            parametros.put("FECHA_INICIO", null);
            parametros.put("FECHA_FIN", null);
        }

        try {
            JasperReportManager.mostrarReporteEnPantalla("ventas_general", parametros);
        } catch (JRException | SQLException ex) {
            ex.printStackTrace();
            String detalle = ex.getMessage();
            Throwable causa = ex.getCause();
            if (causa != null && causa.getMessage() != null) {
                detalle += "\nCausa: " + causa.getMessage();
            }
            JOptionPane.showMessageDialog(
                this,
                "No se pudo generar el reporte: " + detalle,
                "Error JasperReports",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private void cargarReporteHoy() {
        LocalDateTime inicio = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime fin = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        cargarVentas(inicio, fin);
    }
    
    private void cargarReporteSemana() {
        LocalDateTime inicio = LocalDateTime.now().minusDays(7);
        LocalDateTime fin = LocalDateTime.now();
        cargarVentas(inicio, fin);
    }
    
    private void cargarReporteMes() {
        LocalDateTime inicio = LocalDateTime.now().minusDays(30);
        LocalDateTime fin = LocalDateTime.now();
        cargarVentas(inicio, fin);
    }
    
    private void cargarReporteTodas() {
        usarRangoFechas = false;
        ultimoInicio = null;
        ultimoFin = null;

        modeloTabla.setRowCount(0);
        List<Venta> ventas = ventaController.obtenerTodasLasVentas();
        double total = 0.0;
        
        for (Venta v : ventas) {
            modeloTabla.addRow(new Object[]{
                v.getId(),
                v.getFechaVenta().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                String.format("$%.2f", v.getTotal()),
                v.getIdUsuario(),
                v.getIdMesa() != null ? v.getIdMesa() : "N/A"
            });
            total += v.getTotal();
        }
        
        lblCantidadVentas.setText(String.valueOf(ventas.size()));
        lblTotalVentas.setText(String.format("$%.2f", total));
    }
    
    private void cargarVentas(LocalDateTime inicio, LocalDateTime fin) {
        usarRangoFechas = true;
        ultimoInicio = inicio;
        ultimoFin = fin;

        modeloTabla.setRowCount(0);
        List<Venta> ventas = ventaController.obtenerVentasPorFechas(inicio, fin);
        double total = ventaController.calcularTotalVentas(inicio, fin);
        
        for (Venta v : ventas) {
            modeloTabla.addRow(new Object[]{
                v.getId(),
                v.getFechaVenta().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                String.format("$%.2f", v.getTotal()),
                v.getIdUsuario(),
                v.getIdMesa() != null ? v.getIdMesa() : "N/A"
            });
        }
        
        lblCantidadVentas.setText(String.valueOf(ventas.size()));
        lblTotalVentas.setText(String.format("$%.2f", total));
    }
    
    // ==================== CLASE INTERNA MODERNBUTTON ====================
    
    private static class ModernButton extends JButton {
        private Color backgroundColor;
        private Color hoverColor;
        private Color pressedColor;
        private boolean isHovered = false;
        private boolean isPressed = false;
        
        public ModernButton(String text, Color bgColor, Color textColor) {
            super(text);
            this.backgroundColor = bgColor;
            this.hoverColor = bgColor.brighter();
            this.pressedColor = bgColor.darker();
            
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setForeground(textColor);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    isHovered = true;
                    repaint();
                }
                
                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    isHovered = false;
                    repaint();
                }
                
                @Override
                public void mousePressed(java.awt.event.MouseEvent e) {
                    isPressed = true;
                    repaint();
                }
                
                @Override
                public void mouseReleased(java.awt.event.MouseEvent e) {
                    isPressed = false;
                    repaint();
                }
            });
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            Color currentColor = backgroundColor;
            if (isPressed) {
                currentColor = pressedColor;
            } else if (isHovered) {
                currentColor = hoverColor;
            }
            
            if (!isEnabled()) {
                currentColor = new Color(200, 200, 200);
            }
            
            g2.setColor(currentColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
