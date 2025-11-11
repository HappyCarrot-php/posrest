package com.restaurante.view;

import com.restaurante.controller.*;
import com.restaurante.model.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Ventana moderna para registrar ventas con diseño stylish
 */
public class VentaFrame extends JFrame {
    
    private final UsuarioController usuarioController;
    private final ProductoController productoController;
    private final MesaController mesaController;
    private final VentaController ventaController;
    
    // Colores modernos
    private static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    private static final Color PRIMARY_COLOR = new Color(0, 123, 255);
    private static final Color DANGER_COLOR = new Color(220, 53, 69);
    private static final Color BACKGROUND = new Color(248, 249, 250);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(33, 37, 41);
    private static final Color TEXT_SECONDARY = new Color(108, 117, 125);
    
    // Componentes
    private JComboBox<String> cboProductos;
    private JComboBox<String> cboMesas;
    private JSpinner spnCantidad;
    private JTable tablaCarrito;
    private DefaultTableModel modeloCarrito;
    private JLabel lblTotal, lblCambio, lblItemsCount;
    private JTextField txtMontoPagado;
    private ModernButton btnAgregar, btnQuitar, btnRegistrar, btnRegresar, btnLimpiar;
    
    private List<VentaController.ItemVenta> carrito;
    private List<Producto> productosDisponibles;
    private List<Mesa> mesasLibres;
    
    public VentaFrame(UsuarioController usuarioController) {
        this.usuarioController = usuarioController;
        this.productoController = new ProductoController();
        this.mesaController = new MesaController();
        this.ventaController = new VentaController();
        this.carrito = new ArrayList<>();
        
        initComponents();
        cargarDatos();
    }
    
    private void initComponents() {
        setTitle("Registrar Nueva Venta");
        setSize(1300, 800);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND);
        setLayout(new BorderLayout(0, 0));
        
        // Header
        add(createHeader(), BorderLayout.NORTH);
        
        // Contenido principal
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel izquierdo: Selección de productos
        mainPanel.add(createProductPanel(), BorderLayout.WEST);
        
        // Panel centro: Carrito
        mainPanel.add(createCartPanel(), BorderLayout.CENTER);
        
        // Panel derecho: Resumen y pago
        mainPanel.add(createPaymentPanel(), BorderLayout.EAST);
        
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
        
        JLabel iconLabel = new JLabel("🛒");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        
        JLabel lblTitle = new JLabel("Nueva Venta");
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
    
    private JPanel createProductPanel() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(222, 226, 230), 1, true),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        card.setPreferredSize(new Dimension(350, 0));
        
        // Título
        JLabel lblTitle = new JLabel("Agregar Productos");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(TEXT_PRIMARY);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblTitle);
        
        card.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Producto
        card.add(createLabel("Seleccionar Producto"));
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        cboProductos = new JComboBox<>();
        cboProductos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cboProductos.setMaximumSize(new Dimension(300, 45));
        cboProductos.setAlignmentX(Component.LEFT_ALIGNMENT);
        cboProductos.setBackground(Color.WHITE);
        card.add(cboProductos);
        
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Cantidad
        JPanel qtyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        qtyPanel.setBackground(CARD_BG);
        qtyPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        qtyPanel.setMaximumSize(new Dimension(300, 50));
        
        JLabel lblQty = createLabel("Cantidad");
        spnCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        spnCantidad.setFont(new Font("Segoe UI", Font.BOLD, 16));
        ((JSpinner.DefaultEditor) spnCantidad.getEditor()).getTextField().setHorizontalAlignment(JTextField.CENTER);
        spnCantidad.setPreferredSize(new Dimension(100, 45));
        
        qtyPanel.add(lblQty);
        qtyPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        qtyPanel.add(spnCantidad);
        
        card.add(qtyPanel);
        
        card.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Botón agregar
        btnAgregar = new ModernButton("Agregar al Carrito", SUCCESS_COLOR, Color.WHITE);
        btnAgregar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnAgregar.setMaximumSize(new Dimension(300, 50));
        btnAgregar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAgregar.addActionListener(e -> agregarAlCarrito());
        card.add(btnAgregar);
        
        card.add(Box.createRigidArea(new Dimension(0, 35)));
        
        // Mesa
        card.add(createLabel("Mesa (Opcional)"));
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        cboMesas = new JComboBox<>();
        cboMesas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cboMesas.setMaximumSize(new Dimension(300, 45));
        cboMesas.setAlignmentX(Component.LEFT_ALIGNMENT);
        cboMesas.setBackground(Color.WHITE);
        card.add(cboMesas);
        
        card.add(Box.createVerticalGlue());
        
        // Botón limpiar todo
        btnLimpiar = new ModernButton("Limpiar Carrito", DANGER_COLOR, Color.WHITE);
        btnLimpiar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLimpiar.setMaximumSize(new Dimension(300, 45));
        btnLimpiar.addActionListener(e -> limpiarCarrito());
        card.add(btnLimpiar);
        
        return card;
    }
    
    private JPanel createCartPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(BACKGROUND);
        
        // Header del carrito
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND);
        
        JLabel lblTitle = new JLabel("Carrito de Compra");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(TEXT_PRIMARY);
        
        lblItemsCount = new JLabel("0 productos");
        lblItemsCount.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblItemsCount.setForeground(TEXT_SECONDARY);
        
        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(lblItemsCount, BorderLayout.EAST);
        
        // Tabla del carrito
        String[] columnas = {"Producto", "Precio Unit.", "Cantidad", "Subtotal"};
        modeloCarrito = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaCarrito = new JTable(modeloCarrito);
        tablaCarrito.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaCarrito.setRowHeight(50);
        tablaCarrito.setShowGrid(false);
        tablaCarrito.setIntercellSpacing(new Dimension(0, 0));
        tablaCarrito.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaCarrito.setSelectionBackground(new Color(255, 243, 205));
        tablaCarrito.setSelectionForeground(TEXT_PRIMARY);
        
        // Header de tabla
        JTableHeader header = tablaCarrito.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(248, 249, 250));
        header.setForeground(TEXT_SECONDARY);
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(222, 226, 230)));
        
        // Anchos de columna
        tablaCarrito.getColumnModel().getColumn(0).setPreferredWidth(300);
        tablaCarrito.getColumnModel().getColumn(1).setPreferredWidth(120);
        tablaCarrito.getColumnModel().getColumn(2).setPreferredWidth(100);
        tablaCarrito.getColumnModel().getColumn(3).setPreferredWidth(120);
        
        // Centrar columnas numéricas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tablaCarrito.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tablaCarrito.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tablaCarrito.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        
        JScrollPane scrollPane = new JScrollPane(tablaCarrito);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(222, 226, 230), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        // Botón quitar
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(BACKGROUND);
        
        btnQuitar = new ModernButton("Quitar Seleccionado", DANGER_COLOR, Color.WHITE);
        btnQuitar.setPreferredSize(new Dimension(180, 40));
        btnQuitar.addActionListener(e -> quitarDelCarrito());
        bottomPanel.add(btnQuitar);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createPaymentPanel() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(222, 226, 230), 1, true),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        card.setPreferredSize(new Dimension(320, 0));
        
        // Título
        JLabel lblTitle = new JLabel("Resumen de Pago");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(TEXT_PRIMARY);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblTitle);
        
        card.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Total
        JPanel totalPanel = new JPanel();
        totalPanel.setLayout(new BoxLayout(totalPanel, BoxLayout.Y_AXIS));
        totalPanel.setBackground(new Color(40, 167, 69, 20));
        totalPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SUCCESS_COLOR, 2, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        totalPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        totalPanel.setMaximumSize(new Dimension(270, 100));
        
        JLabel lblTotalLabel = new JLabel("TOTAL A PAGAR");
        lblTotalLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTotalLabel.setForeground(TEXT_SECONDARY);
        lblTotalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        lblTotal = new JLabel("$0.00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTotal.setForeground(SUCCESS_COLOR);
        lblTotal.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        totalPanel.add(lblTotalLabel);
        totalPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        totalPanel.add(lblTotal);
        
        card.add(totalPanel);
        
        card.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Monto pagado
        card.add(createLabel("Monto Pagado"));
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        
        txtMontoPagado = new JTextField();
        txtMontoPagado.setFont(new Font("Segoe UI", Font.BOLD, 18));
        txtMontoPagado.setHorizontalAlignment(JTextField.CENTER);
        txtMontoPagado.setMaximumSize(new Dimension(270, 50));
        txtMontoPagado.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtMontoPagado.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(206, 212, 218), 2, true),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        txtMontoPagado.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                calcularCambio();
            }
        });
        card.add(txtMontoPagado);
        
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Cambio
        JPanel cambioPanel = new JPanel();
        cambioPanel.setLayout(new BoxLayout(cambioPanel, BoxLayout.Y_AXIS));
        cambioPanel.setBackground(new Color(248, 249, 250));
        cambioPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(222, 226, 230), 1, true),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        cambioPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        cambioPanel.setMaximumSize(new Dimension(270, 80));
        
        JLabel lblCambioLabel = new JLabel("Cambio:");
        lblCambioLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblCambioLabel.setForeground(TEXT_SECONDARY);
        lblCambioLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        lblCambio = new JLabel("$0.00");
        lblCambio.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblCambio.setForeground(PRIMARY_COLOR);
        lblCambio.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        cambioPanel.add(lblCambioLabel);
        cambioPanel.add(lblCambio);
        
        card.add(cambioPanel);
        
        card.add(Box.createRigidArea(new Dimension(0, 35)));
        
        // Botón registrar venta
        btnRegistrar = new ModernButton("REGISTRAR VENTA", SUCCESS_COLOR, Color.WHITE);
        btnRegistrar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnRegistrar.setMaximumSize(new Dimension(270, 60));
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnRegistrar.addActionListener(e -> registrarVenta());
        card.add(btnRegistrar);
        
        card.add(Box.createVerticalGlue());
        
        return card;
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
    
    private void cargarDatos() {
        // Cargar productos
        productosDisponibles = productoController.obtenerProductosDisponibles();
        cboProductos.removeAllItems();
        cboProductos.addItem("-- Seleccione un producto --");
        for (Producto p : productosDisponibles) {
            cboProductos.addItem(p.getNombre() + " - $" + String.format("%.2f", p.getPrecio()));
        }
        
        // Cargar mesas
        mesasLibres = mesaController.obtenerMesasLibres();
        cboMesas.removeAllItems();
        cboMesas.addItem("-- Sin mesa --");
        for (Mesa m : mesasLibres) {
            cboMesas.addItem("Mesa " + m.getNumero());
        }
    }
    
    private void agregarAlCarrito() {
        int indiceProducto = cboProductos.getSelectedIndex();
        if (indiceProducto <= 0) {
            mostrarMensaje("Por favor seleccione un producto", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Producto producto = productosDisponibles.get(indiceProducto - 1);
        int cantidad = (Integer) spnCantidad.getValue();
        
        // Verificar si el producto ya está en el carrito
        boolean encontrado = false;
        for (VentaController.ItemVenta item : carrito) {
            if (item.getProducto().getId() == producto.getId()) {
                item.setCantidad(item.getCantidad() + cantidad);
                encontrado = true;
                break;
            }
        }
        
        if (!encontrado) {
            carrito.add(new VentaController.ItemVenta(producto, cantidad));
        }
        
        actualizarTablaCarrito();
        spnCantidad.setValue(1);
        cboProductos.setSelectedIndex(0);
    }
    
    private void quitarDelCarrito() {
        int fila = tablaCarrito.getSelectedRow();
        if (fila >= 0) {
            carrito.remove(fila);
            actualizarTablaCarrito();
        } else {
            mostrarMensaje("Por favor seleccione un producto del carrito", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void limpiarCarrito() {
        if (carrito.isEmpty()) {
            return;
        }
        
        int opcion = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro de limpiar todo el carrito?",
            "Confirmar",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (opcion == JOptionPane.YES_OPTION) {
            carrito.clear();
            actualizarTablaCarrito();
            txtMontoPagado.setText("");
        }
    }
    
    private void actualizarTablaCarrito() {
        modeloCarrito.setRowCount(0);
        double total = 0.0;
        
        for (VentaController.ItemVenta item : carrito) {
            modeloCarrito.addRow(new Object[]{
                item.getProducto().getNombre(),
                String.format("$%.2f", item.getProducto().getPrecio()),
                item.getCantidad(),
                String.format("$%.2f", item.getSubtotal())
            });
            total += item.getSubtotal();
        }
        
        lblTotal.setText(String.format("$%.2f", total));
        lblItemsCount.setText(carrito.size() + " producto(s)");
        calcularCambio();
    }
    
    private void calcularCambio() {
        try {
            String totalStr = lblTotal.getText().replace("$", "").trim();
            String pagadoStr = txtMontoPagado.getText().trim();
            
            if (!pagadoStr.isEmpty()) {
                double total = Double.parseDouble(totalStr);
                double pagado = Double.parseDouble(pagadoStr);
                double cambio = pagado - total;
                
                lblCambio.setText(String.format("$%.2f", cambio));
                lblCambio.setForeground(cambio < 0 ? DANGER_COLOR : PRIMARY_COLOR);
            } else {
                lblCambio.setText("$0.00");
                lblCambio.setForeground(PRIMARY_COLOR);
            }
        } catch (NumberFormatException e) {
            lblCambio.setText("$0.00");
            lblCambio.setForeground(PRIMARY_COLOR);
        }
    }
    
    private void registrarVenta() {
        if (carrito.isEmpty()) {
            mostrarMensaje("El carrito está vacío", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String montoPagadoStr = txtMontoPagado.getText().trim();
        if (montoPagadoStr.isEmpty()) {
            mostrarMensaje("Por favor ingrese el monto pagado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            double montoPagado = Double.parseDouble(montoPagadoStr);
            double total = ventaController.calcularTotal(carrito);
            
            if (montoPagado < total) {
                mostrarMensaje("El monto pagado es insuficiente", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Obtener ID de mesa si se seleccionó
            Integer idMesa = null;
            int indiceMesa = cboMesas.getSelectedIndex();
            if (indiceMesa > 0) {
                idMesa = mesasLibres.get(indiceMesa - 1).getId();
            }
            
            // Registrar venta
            String folio = ventaController.registrarVenta(
                usuarioController.getUsuarioActual().getId(),
                idMesa,
                carrito,
                montoPagado
            );
            
            if (folio != null) {
                // Mostrar ticket
                Ticket ticket = ventaController.buscarTicketPorFolio(folio);
                if (ticket != null) {
                    TicketFrame ticketFrame = new TicketFrame(ticket, carrito, usuarioController.getUsuarioActual());
                    ticketFrame.setVisible(true);
                }
                
                mostrarMensaje(
                    "Venta registrada correctamente\n\nFolio: " + folio + "\nTotal: $" + String.format("%.2f", total),
                    "Venta Exitosa",
                    JOptionPane.INFORMATION_MESSAGE
                );
                
                // Limpiar todo
                carrito.clear();
                actualizarTablaCarrito();
                txtMontoPagado.setText("");
                cboProductos.setSelectedIndex(0);
                cboMesas.setSelectedIndex(0);
                cargarDatos();
            } else {
                mostrarMensaje("Error al registrar la venta", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException ex) {
            mostrarMensaje("El monto pagado debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
        }
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
