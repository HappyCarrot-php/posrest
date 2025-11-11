package com.restaurante.view;

import com.restaurante.controller.UsuarioController;
import com.restaurante.model.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

/**
 * Ventana de gestión de usuarios con diseño moderno (solo administradores).
 */
public class UsuarioFrame extends JFrame {
    
    private final UsuarioController usuarioController;
    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;
    private JTextField txtNombre, txtCorreo, txtContraseña;
    private JComboBox<String> cboRol;
    private ModernButton btnGuardar, btnActualizar, btnEliminar, btnLimpiar;
    private int idUsuarioSeleccionado = -1;
    
    // Colores modernos
    private static final Color PRIMARY_COLOR = new Color(74, 144, 226);
    private static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    private static final Color DANGER_COLOR = new Color(220, 53, 69);
    private static final Color WARNING_COLOR = new Color(255, 193, 7);
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Color TEXT_PRIMARY = new Color(33, 37, 41);
    private static final Color TEXT_SECONDARY = new Color(108, 117, 125);
    
    public UsuarioFrame() {
        this.usuarioController = new UsuarioController();
        inicializarComponentes();
        cargarUsuarios();
    }
    
    private void inicializarComponentes() {
        setTitle("Gestión de Usuarios");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Header con título y botón regresar
        add(createHeader(), BorderLayout.NORTH);
        
        // Panel principal con dos columnas
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel izquierdo: Formulario
        mainPanel.add(createFormPanel());
        
        // Panel derecho: Tabla
        mainPanel.add(createTablePanel());
        
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
        JLabel lblTitle = new JLabel("Gestión de Usuarios");
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
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        
        // Título
        JLabel lblTitulo = new JLabel("Datos del Usuario");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(TEXT_PRIMARY);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblTitulo);
        
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Campo: Nombre
        panel.add(createFieldLabel("Nombre Completo"));
        txtNombre = createStyledTextField();
        txtNombre.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.add(txtNombre);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Campo: Correo
        panel.add(createFieldLabel("Correo Electrónico"));
        txtCorreo = createStyledTextField();
        txtCorreo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.add(txtCorreo);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Campo: Contraseña
        panel.add(createFieldLabel("Contraseña"));
        txtContraseña = createStyledTextField();
        txtContraseña.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.add(txtContraseña);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Campo: Rol
        panel.add(createFieldLabel("Rol"));
        cboRol = new JComboBox<>(new String[]{"administrador", "cajero", "mesero"});
        cboRol.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboRol.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cboRol.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(cboRol);
        
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Botones
        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        btnPanel.setOpaque(false);
        btnPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        btnPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        btnGuardar = new ModernButton("Guardar", SUCCESS_COLOR, Color.WHITE);
        btnActualizar = new ModernButton("Actualizar", PRIMARY_COLOR, Color.WHITE);
        btnEliminar = new ModernButton("Eliminar", DANGER_COLOR, Color.WHITE);
        btnLimpiar = new ModernButton("Limpiar", TEXT_SECONDARY, Color.WHITE);
        
        btnGuardar.addActionListener(e -> guardarUsuario());
        btnActualizar.addActionListener(e -> actualizarUsuario());
        btnEliminar.addActionListener(e -> eliminarUsuario());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        
        btnPanel.add(btnGuardar);
        btnPanel.add(btnActualizar);
        btnPanel.add(btnEliminar);
        btnPanel.add(btnLimpiar);
        
        panel.add(btnPanel);
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        
        // Título
        JLabel lblTitulo = new JLabel("Lista de Usuarios");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(TEXT_PRIMARY);
        panel.add(lblTitulo, BorderLayout.NORTH);
        
        // Tabla
        String[] columnas = {"ID", "Nombre", "Correo", "Rol"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaUsuarios.setRowHeight(35);
        tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaUsuarios.setSelectionBackground(new Color(74, 144, 226, 50));
        tablaUsuarios.setSelectionForeground(TEXT_PRIMARY);
        tablaUsuarios.setShowGrid(false);
        tablaUsuarios.setIntercellSpacing(new Dimension(0, 0));
        
        // Header de tabla
        tablaUsuarios.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaUsuarios.getTableHeader().setBackground(BACKGROUND_COLOR);
        tablaUsuarios.getTableHeader().setForeground(TEXT_PRIMARY);
        tablaUsuarios.getTableHeader().setPreferredSize(new Dimension(0, 40));
        
        // Centrar columna ID
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tablaUsuarios.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tablaUsuarios.getColumnModel().getColumn(0).setPreferredWidth(50);
        
        tablaUsuarios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarUsuarioSeleccionado();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tablaUsuarios);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }
    
    private void guardarUsuario() {
        String nombre = txtNombre.getText().trim();
        String correo = txtCorreo.getText().trim();
        String contraseña = txtContraseña.getText().trim();
        String rol = (String) cboRol.getSelectedItem();
        
        if (nombre.isEmpty() || correo.isEmpty() || contraseña.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (usuarioController.crearUsuario(nombre, correo, contraseña, rol)) {
            JOptionPane.showMessageDialog(this, "Usuario guardado correctamente");
            limpiarFormulario();
            cargarUsuarios();
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar usuario (verifique que el correo no esté registrado)", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void actualizarUsuario() {
        if (idUsuarioSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String nombre = txtNombre.getText().trim();
        String correo = txtCorreo.getText().trim();
        String contraseña = txtContraseña.getText().trim();
        String rol = (String) cboRol.getSelectedItem();
        
        if (nombre.isEmpty() || correo.isEmpty() || contraseña.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Usuario usuario = usuarioController.buscarUsuarioPorId(idUsuarioSeleccionado);
        usuario.setNombre(nombre);
        usuario.setCorreo(correo);
        usuario.setContraseñaHash(contraseña);
        usuario.setRol(rol);
        
        if (usuarioController.actualizarUsuario(usuario)) {
            JOptionPane.showMessageDialog(this, "Usuario actualizado correctamente");
            limpiarFormulario();
            cargarUsuarios();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar usuario", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void eliminarUsuario() {
        if (idUsuarioSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int opcion = JOptionPane.showConfirmDialog(this, "¿Eliminar este usuario?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            if (usuarioController.eliminarUsuario(idUsuarioSeleccionado)) {
                JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente");
                limpiarFormulario();
                cargarUsuarios();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar usuario", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void cargarUsuarios() {
        modeloTabla.setRowCount(0);
        List<Usuario> usuarios = usuarioController.obtenerTodosLosUsuarios();
        for (Usuario u : usuarios) {
            modeloTabla.addRow(new Object[]{
                u.getId(),
                u.getNombre(),
                u.getCorreo(),
                u.getRol().toUpperCase()
            });
        }
    }
    
    private void cargarUsuarioSeleccionado() {
        int fila = tablaUsuarios.getSelectedRow();
        if (fila >= 0) {
            idUsuarioSeleccionado = (int) modeloTabla.getValueAt(fila, 0);
            Usuario u = usuarioController.buscarUsuarioPorId(idUsuarioSeleccionado);
            if (u != null) {
                txtNombre.setText(u.getNombre());
                txtCorreo.setText(u.getCorreo());
                txtContraseña.setText(u.getContraseñaHash());
                cboRol.setSelectedItem(u.getRol());
            }
        }
    }
    
    private void limpiarFormulario() {
        txtNombre.setText("");
        txtCorreo.setText("");
        txtContraseña.setText("");
        cboRol.setSelectedIndex(0);
        idUsuarioSeleccionado = -1;
        tablaUsuarios.clearSelection();
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
