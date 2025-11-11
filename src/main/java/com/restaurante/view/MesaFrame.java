package com.restaurante.view;

import com.restaurante.controller.MesaController;
import com.restaurante.model.Mesa;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Ventana moderna de gestión de mesas con diseño stylish
 */
public class MesaFrame extends JFrame {
    
    private final MesaController mesaController;
    
    // Colores modernos
    private static final Color PRIMARY_COLOR = new Color(111, 66, 193);
    private static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    private static final Color DANGER_COLOR = new Color(220, 53, 69);
    private static final Color BACKGROUND = new Color(248, 249, 250);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(33, 37, 41);
    private static final Color TEXT_SECONDARY = new Color(108, 117, 125);
    
    // Estados de mesa con colores
    private static final Color LIBRE_COLOR = new Color(40, 167, 69);
    private static final Color OCUPADA_COLOR = new Color(220, 53, 69);
    private static final Color RESERVADA_COLOR = new Color(255, 193, 7);
    
    // Componentes
    private JTable tablaMesas;
    private DefaultTableModel modeloTabla;
    private JTextField txtNumero;
    private JComboBox<String> cboEstado;
    private ModernButton btnGuardar, btnActualizar, btnEliminar, btnLimpiar, btnRegresar;
    private JPanel panelMesasVisual;
    private int idMesaSeleccionada = -1;
    
    public MesaFrame() {
        this.mesaController = new MesaController();
        initComponents();
        cargarMesas();
    }
    
    private void initComponents() {
        setTitle("Gestión de Mesas");
        setSize(1300, 750);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND);
        setLayout(new BorderLayout(0, 0));
        
        // Header con botón regresar
        add(createHeader(), BorderLayout.NORTH);
        
        // Contenido principal
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel izquierdo: Formulario
        mainPanel.add(createFormPanel(), BorderLayout.WEST);
        
        // Panel central: Vista visual de mesas
        mainPanel.add(createVisualPanel(), BorderLayout.CENTER);
        
        // Panel derecho: Tabla
        mainPanel.add(createTablePanel(), BorderLayout.EAST);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        // Título
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setBackground(Color.WHITE);
        
        JLabel iconLabel = new JLabel("🪑");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        
        JLabel lblTitle = new JLabel("Gestión de Mesas");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(TEXT_PRIMARY);
        
        leftPanel.add(iconLabel);
        leftPanel.add(lblTitle);
        
        // Botón regresar
        btnRegresar = new ModernButton("Regresar", TEXT_SECONDARY, Color.WHITE);
        btnRegresar.setPreferredSize(new Dimension(130, 40));
        btnRegresar.addActionListener(e -> dispose());
        
        header.add(leftPanel, BorderLayout.WEST);
        header.add(btnRegresar, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createFormPanel() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(222, 226, 230), 1, true),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        card.setPreferredSize(new Dimension(300, 0));
        
        // Título del formulario
        JLabel lblFormTitle = new JLabel("Datos de la Mesa");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblFormTitle.setForeground(TEXT_PRIMARY);
        lblFormTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblFormTitle);
        
        card.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Campo: Número
        card.add(createLabel("Número de Mesa"));
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        txtNumero = createStyledTextField();
        card.add(txtNumero);
        
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Campo: Estado
        card.add(createLabel("Estado"));
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        
        cboEstado = new JComboBox<>(new String[]{"libre", "ocupada", "reservada"});
        cboEstado.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboEstado.setMaximumSize(new Dimension(250, 40));
        cboEstado.setAlignmentX(Component.LEFT_ALIGNMENT);
        cboEstado.setBackground(Color.WHITE);
        cboEstado.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(206, 212, 218), 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        card.add(cboEstado);
        
        card.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Leyenda de colores
        JPanel legendPanel = createLegendPanel();
        legendPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(legendPanel);
        
        card.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Botones
        btnGuardar = new ModernButton("Guardar", SUCCESS_COLOR, Color.WHITE);
        btnGuardar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnGuardar.setMaximumSize(new Dimension(250, 45));
        btnGuardar.addActionListener(e -> guardarMesa());
        card.add(btnGuardar);
        
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        
        btnActualizar = new ModernButton("Actualizar", PRIMARY_COLOR, Color.WHITE);
        btnActualizar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnActualizar.setMaximumSize(new Dimension(250, 45));
        btnActualizar.addActionListener(e -> actualizarMesa());
        card.add(btnActualizar);
        
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        
        btnEliminar = new ModernButton("Eliminar", DANGER_COLOR, Color.WHITE);
        btnEliminar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnEliminar.setMaximumSize(new Dimension(250, 45));
        btnEliminar.addActionListener(e -> eliminarMesa());
        card.add(btnEliminar);
        
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        
        btnLimpiar = new ModernButton("Limpiar", TEXT_SECONDARY, Color.WHITE);
        btnLimpiar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLimpiar.setMaximumSize(new Dimension(250, 45));
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        card.add(btnLimpiar);
        
        card.add(Box.createVerticalGlue());
        
        return card;
    }
    
    private JPanel createLegendPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(222, 226, 230), 1),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        
        JLabel lblTitle = new JLabel("Estado de Mesas:");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblTitle.setForeground(TEXT_SECONDARY);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblTitle);
        
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        
        panel.add(createLegendItem("●", LIBRE_COLOR, "Libre"));
        panel.add(createLegendItem("●", OCUPADA_COLOR, "Ocupada"));
        panel.add(createLegendItem("●", RESERVADA_COLOR, "Reservada"));
        
        return panel;
    }
    
    private JPanel createLegendItem(String symbol, Color color, String text) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        item.setBackground(new Color(248, 249, 250));
        item.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblSymbol = new JLabel(symbol);
        lblSymbol.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSymbol.setForeground(color);
        
        JLabel lblText = new JLabel(text);
        lblText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblText.setForeground(TEXT_PRIMARY);
        
        item.add(lblSymbol);
        item.add(lblText);
        
        return item;
    }
    
    private JPanel createVisualPanel() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(BACKGROUND);
        
        JLabel lblTitle = new JLabel("Vista del Restaurante");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(TEXT_PRIMARY);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        panelMesasVisual = new JPanel(new GridLayout(0, 4, 15, 15));
        panelMesasVisual.setBackground(CARD_BG);
        panelMesasVisual.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(222, 226, 230), 1, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JScrollPane scroll = new JScrollPane(panelMesasVisual);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(CARD_BG);
        
        container.add(lblTitle, BorderLayout.NORTH);
        container.add(scroll, BorderLayout.CENTER);
        
        return container;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(BACKGROUND);
        panel.setPreferredSize(new Dimension(350, 0));
        
        JLabel lblTitle = new JLabel("Lista de Mesas");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(TEXT_PRIMARY);
        
        // Tabla
        String[] columnas = {"#", "Número", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaMesas = new JTable(modeloTabla);
        tablaMesas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaMesas.setRowHeight(40);
        tablaMesas.setShowGrid(false);
        tablaMesas.setIntercellSpacing(new Dimension(0, 0));
        tablaMesas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaMesas.setSelectionBackground(new Color(232, 244, 253));
        tablaMesas.setSelectionForeground(TEXT_PRIMARY);
        
        // Header de tabla
        JTableHeader header = tablaMesas.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(248, 249, 250));
        header.setForeground(TEXT_SECONDARY);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(222, 226, 230)));
        
        // Centrar columnas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tablaMesas.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tablaMesas.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tablaMesas.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        
        tablaMesas.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaMesas.getColumnModel().getColumn(1).setPreferredWidth(100);
        tablaMesas.getColumnModel().getColumn(2).setPreferredWidth(150);
        
        tablaMesas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarMesaSeleccionada();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tablaMesas);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(222, 226, 230), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(250, 40));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(206, 212, 218), 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return field;
    }
    
    private void guardarMesa() {
        String numeroStr = txtNumero.getText().trim();
        String estado = (String) cboEstado.getSelectedItem();
        
        if (numeroStr.isEmpty()) {
            mostrarMensaje("Por favor ingrese el número de mesa", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int numero = Integer.parseInt(numeroStr);
            if (numero <= 0) {
                mostrarMensaje("El número de mesa debe ser mayor a 0", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (mesaController.crearMesa(numero, estado)) {
                mostrarMensaje("Mesa guardada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarMesas();
            } else {
                mostrarMensaje("Error al guardar mesa (puede que el número ya exista)", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            mostrarMensaje("El número de mesa debe ser un valor numérico", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void actualizarMesa() {
        if (idMesaSeleccionada == -1) {
            mostrarMensaje("Por favor seleccione una mesa de la tabla", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String numeroStr = txtNumero.getText().trim();
        String estado = (String) cboEstado.getSelectedItem();
        
        if (numeroStr.isEmpty()) {
            mostrarMensaje("Por favor ingrese el número de mesa", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int numero = Integer.parseInt(numeroStr);
            if (numero <= 0) {
                mostrarMensaje("El número de mesa debe ser mayor a 0", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Mesa mesa = mesaController.buscarMesaPorId(idMesaSeleccionada);
            mesa.setNumero(numero);
            mesa.setEstado(estado);
            
            if (mesaController.actualizarMesa(mesa)) {
                mostrarMensaje("Mesa actualizada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarMesas();
            } else {
                mostrarMensaje("Error al actualizar mesa", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            mostrarMensaje("El número de mesa debe ser un valor numérico", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void eliminarMesa() {
        if (idMesaSeleccionada == -1) {
            mostrarMensaje("Por favor seleccione una mesa de la tabla", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int opcion = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro de eliminar esta mesa?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (opcion == JOptionPane.YES_OPTION) {
            if (mesaController.eliminarMesa(idMesaSeleccionada)) {
                mostrarMensaje("Mesa eliminada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarMesas();
            } else {
                mostrarMensaje("Error al eliminar mesa", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void cargarMesas() {
        modeloTabla.setRowCount(0);
        panelMesasVisual.removeAll();
        
        List<Mesa> mesas = mesaController.obtenerTodasLasMesas();
        
        for (Mesa m : mesas) {
            // Agregar a tabla
            modeloTabla.addRow(new Object[]{
                m.getId(),
                m.getNumero(),
                m.getEstado().toUpperCase()
            });
            
            // Agregar a vista visual
            panelMesasVisual.add(createMesaCard(m));
        }
        
        panelMesasVisual.revalidate();
        panelMesasVisual.repaint();
    }
    
    private JPanel createMesaCard(Mesa mesa) {
        Color cardColor;
        String estado = mesa.getEstado().toLowerCase();
        if (estado.equals("ocupada")) {
            cardColor = OCUPADA_COLOR;
        } else if (estado.equals("reservada")) {
            cardColor = RESERVADA_COLOR;
        } else {
            cardColor = LIBRE_COLOR;
        }
        
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(cardColor);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(cardColor.darker(), 2, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setPreferredSize(new Dimension(120, 120));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Icono
        JLabel lblIcon = new JLabel("🪑");
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Número
        JLabel lblNumero = new JLabel("Mesa " + mesa.getNumero());
        lblNumero.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblNumero.setForeground(Color.WHITE);
        lblNumero.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Estado
        JLabel lblEstado = new JLabel(mesa.getEstado().toUpperCase());
        lblEstado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblEstado.setForeground(Color.WHITE);
        lblEstado.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(lblIcon);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(lblNumero);
        card.add(lblEstado);
        
        // Click en card para seleccionar
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarMesa(mesa);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(cardColor.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(cardColor);
            }
        });
        
        return card;
    }
    
    private void seleccionarMesa(Mesa mesa) {
        idMesaSeleccionada = mesa.getId();
        txtNumero.setText(String.valueOf(mesa.getNumero()));
        cboEstado.setSelectedItem(mesa.getEstado());
        
        // Seleccionar en tabla
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            if ((int) modeloTabla.getValueAt(i, 0) == mesa.getId()) {
                tablaMesas.setRowSelectionInterval(i, i);
                break;
            }
        }
    }
    
    private void cargarMesaSeleccionada() {
        int fila = tablaMesas.getSelectedRow();
        if (fila >= 0) {
            idMesaSeleccionada = (int) modeloTabla.getValueAt(fila, 0);
            Mesa m = mesaController.buscarMesaPorId(idMesaSeleccionada);
            if (m != null) {
                txtNumero.setText(String.valueOf(m.getNumero()));
                cboEstado.setSelectedItem(m.getEstado());
            }
        }
    }
    
    private void limpiarFormulario() {
        txtNumero.setText("");
        cboEstado.setSelectedIndex(0);
        idMesaSeleccionada = -1;
        tablaMesas.clearSelection();
    }
    
    private void mostrarMensaje(String mensaje, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, tipo);
    }
    
    // ==================== CLASE INTERNA: BOTÓN MODERNO ====================
    
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
            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    repaint();
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    repaint();
                }
                
                @Override
                public void mousePressed(MouseEvent e) {
                    isPressed = true;
                    repaint();
                }
                
                @Override
                public void mouseReleased(MouseEvent e) {
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
            
            // Sombra
            g2.setColor(new Color(0, 0, 0, 20));
            g2.fillRoundRect(2, 3, getWidth() - 4, getHeight() - 4, 8, 8);
            
            // Fondo
            g2.setColor(currentColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight() - 3, 8, 8);
            
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
