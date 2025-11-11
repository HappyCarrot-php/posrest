package com.restaurante.view;

import com.restaurante.controller.ProductoController;
import com.restaurante.model.Producto;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Ventana moderna de gestión de productos con diseño stylish
 */
public class ProductoFrame extends JFrame {
    
    private final ProductoController productoController;
    
    // Colores modernos
    private static final Color PRIMARY_COLOR = new Color(0, 123, 255);
    private static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    private static final Color DANGER_COLOR = new Color(220, 53, 69);
    private static final Color WARNING_COLOR = new Color(255, 193, 7);
    private static final Color BACKGROUND = new Color(248, 249, 250);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(33, 37, 41);
    private static final Color TEXT_SECONDARY = new Color(108, 117, 125);
    
    // Componentes
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JTextField txtNombre, txtCategoria, txtPrecio, txtBuscar;
    private JCheckBox chkDisponible;
    private ModernButton btnGuardar, btnActualizar, btnEliminar, btnLimpiar, btnRegresar;
    private int idProductoSeleccionado = -1;
    
    public ProductoFrame() {
        this.productoController = new ProductoController();
        initComponents();
        cargarProductos();
    }
    
    private void initComponents() {
        setTitle("Gestión de Productos");
        setSize(1200, 700);
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
        
        // Panel derecho: Tabla
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);
        
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
        
        JLabel iconLabel = new JLabel("📦");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        
        JLabel lblTitle = new JLabel("Gestión de Productos");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(TEXT_PRIMARY);
        
        leftPanel.add(iconLabel);
        leftPanel.add(lblTitle);
        
        // Botón regresar
        btnRegresar = new ModernButton("Regresar", TEXT_SECONDARY, Color.WHITE);
        btnRegresar.setPreferredSize(new Dimension(130, 40));
        btnRegresar.addActionListener(e -> {
            dispose();
        });
        
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
        card.setPreferredSize(new Dimension(350, 0));
        
        // Título del formulario
        JLabel lblFormTitle = new JLabel("Datos del Producto");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblFormTitle.setForeground(TEXT_PRIMARY);
        lblFormTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblFormTitle);
        
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Campo: Nombre
        card.add(createLabel("Nombre del Producto"));
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        txtNombre = createStyledTextField();
        card.add(txtNombre);
        
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Campo: Categoría
        card.add(createLabel("Categoría"));
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        txtCategoria = createStyledTextField();
        card.add(txtCategoria);
        
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Campo: Precio
        card.add(createLabel("Precio"));
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        txtPrecio = createStyledTextField();
        card.add(txtPrecio);
        
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Campo: Disponible
        JPanel checkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        checkPanel.setBackground(CARD_BG);
        checkPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        chkDisponible = new JCheckBox("Producto disponible");
        chkDisponible.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        chkDisponible.setForeground(TEXT_PRIMARY);
        chkDisponible.setBackground(CARD_BG);
        chkDisponible.setSelected(true);
        chkDisponible.setFocusPainted(false);
        
        checkPanel.add(chkDisponible);
        card.add(checkPanel);
        
        card.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Botones
        btnGuardar = new ModernButton("Guardar", SUCCESS_COLOR, Color.WHITE);
        btnGuardar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnGuardar.setMaximumSize(new Dimension(300, 45));
        btnGuardar.addActionListener(e -> guardarProducto());
        card.add(btnGuardar);
        
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        
        btnActualizar = new ModernButton("Actualizar", PRIMARY_COLOR, Color.WHITE);
        btnActualizar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnActualizar.setMaximumSize(new Dimension(300, 45));
        btnActualizar.addActionListener(e -> actualizarProducto());
        card.add(btnActualizar);
        
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        
        btnEliminar = new ModernButton("Eliminar", DANGER_COLOR, Color.WHITE);
        btnEliminar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnEliminar.setMaximumSize(new Dimension(300, 45));
        btnEliminar.addActionListener(e -> eliminarProducto());
        card.add(btnEliminar);
        
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        
        btnLimpiar = new ModernButton("Limpiar", TEXT_SECONDARY, Color.WHITE);
        btnLimpiar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLimpiar.setMaximumSize(new Dimension(300, 45));
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        card.add(btnLimpiar);
        
        card.add(Box.createVerticalGlue());
        
        return card;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(BACKGROUND);
        
        // Barra de búsqueda
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        searchPanel.setBackground(BACKGROUND);
        
        JLabel lblSearch = new JLabel("🔍 Buscar:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSearch.setForeground(TEXT_PRIMARY);
        
        txtBuscar = new JTextField(30);
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtBuscar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(206, 212, 218), 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        txtBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                buscarProductos(txtBuscar.getText());
            }
        });
        
        searchPanel.add(lblSearch);
        searchPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        searchPanel.add(txtBuscar);
        
        // Tabla
        String[] columnas = {"ID", "Nombre", "Categoría", "Precio", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaProductos.setRowHeight(40);
        tablaProductos.setShowGrid(false);
        tablaProductos.setIntercellSpacing(new Dimension(0, 0));
        tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaProductos.setSelectionBackground(new Color(232, 244, 253));
        tablaProductos.setSelectionForeground(TEXT_PRIMARY);
        
        // Header de tabla
        JTableHeader header = tablaProductos.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(248, 249, 250));
        header.setForeground(TEXT_SECONDARY);
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(222, 226, 230)));
        
        // Centrar columnas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tablaProductos.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tablaProductos.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tablaProductos.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        
        // Anchos de columna
        tablaProductos.getColumnModel().getColumn(0).setPreferredWidth(60);
        tablaProductos.getColumnModel().getColumn(1).setPreferredWidth(250);
        tablaProductos.getColumnModel().getColumn(2).setPreferredWidth(150);
        tablaProductos.getColumnModel().getColumn(3).setPreferredWidth(100);
        tablaProductos.getColumnModel().getColumn(4).setPreferredWidth(100);
        
        tablaProductos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarProductoSeleccionado();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(222, 226, 230), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panel.add(searchPanel, BorderLayout.NORTH);
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
        field.setMaximumSize(new Dimension(300, 40));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(206, 212, 218), 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return field;
    }
    
    private void guardarProducto() {
        String nombre = txtNombre.getText().trim();
        String categoria = txtCategoria.getText().trim();
        String precioStr = txtPrecio.getText().trim();
        boolean disponible = chkDisponible.isSelected();
        
        if (nombre.isEmpty() || categoria.isEmpty() || precioStr.isEmpty()) {
            mostrarMensaje("Por favor complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            double precio = Double.parseDouble(precioStr);
            if (precio <= 0) {
                mostrarMensaje("El precio debe ser mayor a 0", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (productoController.crearProducto(nombre, categoria, precio, disponible)) {
                mostrarMensaje("Producto guardado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarProductos();
            } else {
                mostrarMensaje("Error al guardar el producto", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            mostrarMensaje("El precio debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void actualizarProducto() {
        if (idProductoSeleccionado == -1) {
            mostrarMensaje("Por favor seleccione un producto de la tabla", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String nombre = txtNombre.getText().trim();
        String categoria = txtCategoria.getText().trim();
        String precioStr = txtPrecio.getText().trim();
        boolean disponible = chkDisponible.isSelected();
        
        if (nombre.isEmpty() || categoria.isEmpty() || precioStr.isEmpty()) {
            mostrarMensaje("Por favor complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            double precio = Double.parseDouble(precioStr);
            if (precio <= 0) {
                mostrarMensaje("El precio debe ser mayor a 0", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Producto producto = productoController.buscarProductoPorId(idProductoSeleccionado);
            producto.setNombre(nombre);
            producto.setCategoria(categoria);
            producto.setPrecio(precio);
            producto.setDisponible(disponible);
            
            if (productoController.actualizarProducto(producto)) {
                mostrarMensaje("Producto actualizado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarProductos();
            } else {
                mostrarMensaje("Error al actualizar el producto", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            mostrarMensaje("El precio debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void eliminarProducto() {
        if (idProductoSeleccionado == -1) {
            mostrarMensaje("Por favor seleccione un producto de la tabla", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int opcion = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro de eliminar este producto?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (opcion == JOptionPane.YES_OPTION) {
            if (productoController.eliminarProducto(idProductoSeleccionado)) {
                mostrarMensaje("Producto eliminado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarProductos();
            } else {
                mostrarMensaje("Error al eliminar el producto", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void cargarProductos() {
        modeloTabla.setRowCount(0);
        List<Producto> productos = productoController.obtenerTodosLosProductos();
        for (Producto p : productos) {
            modeloTabla.addRow(new Object[]{
                p.getId(),
                p.getNombre(),
                p.getCategoria(),
                String.format("$%.2f", p.getPrecio()),
                p.isDisponible() ? "Disponible" : "No disponible"
            });
        }
    }
    
    private void buscarProductos(String termino) {
        modeloTabla.setRowCount(0);
        List<Producto> productos = productoController.obtenerTodosLosProductos();
        
        for (Producto p : productos) {
            if (termino.isEmpty() || 
                p.getNombre().toLowerCase().contains(termino.toLowerCase()) ||
                p.getCategoria().toLowerCase().contains(termino.toLowerCase())) {
                
                modeloTabla.addRow(new Object[]{
                    p.getId(),
                    p.getNombre(),
                    p.getCategoria(),
                    String.format("$%.2f", p.getPrecio()),
                    p.isDisponible() ? "Disponible" : "No disponible"
                });
            }
        }
    }
    
    private void cargarProductoSeleccionado() {
        int fila = tablaProductos.getSelectedRow();
        if (fila >= 0) {
            idProductoSeleccionado = (int) modeloTabla.getValueAt(fila, 0);
            Producto p = productoController.buscarProductoPorId(idProductoSeleccionado);
            if (p != null) {
                txtNombre.setText(p.getNombre());
                txtCategoria.setText(p.getCategoria());
                txtPrecio.setText(String.valueOf(p.getPrecio()));
                chkDisponible.setSelected(p.isDisponible());
            }
        }
    }
    
    private void limpiarFormulario() {
        txtNombre.setText("");
        txtCategoria.setText("");
        txtPrecio.setText("");
        chkDisponible.setSelected(true);
        idProductoSeleccionado = -1;
        tablaProductos.clearSelection();
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
