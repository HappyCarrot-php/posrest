package com.restaurante.view;

import com.restaurante.controller.UsuarioController;
import com.restaurante.model.Usuario;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Ventana de inicio de sesión con diseño moderno y stylish
 * Permite autenticar usuarios del sistema
 */
public class LoginFrame extends JFrame {
    
    private UsuarioController usuarioController;
    
    // Colores modernos y vibrantes
    private static final Color PRIMARY_COLOR = new Color(74, 144, 226);      // Azul moderno
    private static final Color PRIMARY_DARK = new Color(41, 98, 255);        // Azul oscuro
    private static final Color BACKGROUND_COLOR = new Color(245, 247, 250);  // Gris muy claro
    private static final Color CARD_COLOR = Color.WHITE;                     // Blanco
    private static final Color TEXT_PRIMARY = new Color(33, 37, 41);         // Negro suave
    private static final Color TEXT_SECONDARY = new Color(108, 117, 125);    // Gris
    private static final Color SUCCESS_COLOR = new Color(40, 167, 69);       // Verde éxito
    private static final Color ERROR_COLOR = new Color(220, 53, 69);         // Rojo error
    
    // Componentes
    private JTextField txtCorreo;
    private JPasswordField txtPassword;
    private ModernButton btnLogin;
    private JLabel lblMensaje;
    
    public LoginFrame() {
        this.usuarioController = new UsuarioController();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Sistema POS Restaurante");
        setSize(500, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Panel principal con gradiente
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradiente de fondo
                GradientPaint gradient = new GradientPaint(
                    0, 0, PRIMARY_COLOR,
                    0, getHeight(), PRIMARY_DARK
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        
        // Card central con formulario
        JPanel cardPanel = createCardPanel();
        mainPanel.add(cardPanel);
        
        setContentPane(mainPanel);
    }
    
    private JPanel createCardPanel() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(20, new Color(0, 0, 0, 20)),
            BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));
        card.setPreferredSize(new Dimension(380, 500));
        
        // Logo/Icono
        JLabel iconLabel = new JLabel("🍽️");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 72));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(iconLabel);
        
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Título
        JLabel lblTitulo = new JLabel("Bienvenido");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(TEXT_PRIMARY);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblTitulo);
        
        // Subtítulo
        JLabel lblSubtitulo = new JLabel("Inicia sesión para continuar");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(TEXT_SECONDARY);
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblSubtitulo);
        
        card.add(Box.createRigidArea(new Dimension(0, 40)));
        
        // Campo de correo
        JLabel lblCorreo = new JLabel("Correo Electrónico");
        lblCorreo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCorreo.setForeground(TEXT_PRIMARY);
        lblCorreo.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblCorreo);
        
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        
        txtCorreo = createStyledTextField("usuario@ejemplo.com");
        txtCorreo.setMaximumSize(new Dimension(300, 45));
        txtCorreo.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(txtCorreo);
        
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Campo de contraseña
        JLabel lblPassword = new JLabel("Contraseña");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPassword.setForeground(TEXT_PRIMARY);
        lblPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblPassword);
        
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        
        txtPassword = createStyledPasswordField();
        txtPassword.setMaximumSize(new Dimension(300, 45));
        txtPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtPassword.addActionListener(e -> login());
        card.add(txtPassword);
        
        card.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Botón Login
        btnLogin = new ModernButton("Iniciar Sesión", PRIMARY_COLOR, Color.WHITE);
        btnLogin.setMaximumSize(new Dimension(300, 50));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.addActionListener(e -> login());
        card.add(btnLogin);
        
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Mensaje de estado
        lblMensaje = new JLabel(" ");
        lblMensaje.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblMensaje);
        
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Información de ayuda
        JLabel lblAyuda = new JLabel("<html><center><b>Usuarios demo:</b><br>admin@restaurante.com / admin123<br>cajero@restaurante.com / cajero123</center></html>");
        lblAyuda.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblAyuda.setForeground(TEXT_SECONDARY);
        lblAyuda.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblAyuda);
        
        return card;
    }
    
    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                if (!isOpaque() && getBorder() instanceof RoundedBorder) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2.dispose();
                }
                super.paintComponent(g);
            }
        };
        
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(TEXT_PRIMARY);
        field.setBackground(BACKGROUND_COLOR);
        field.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(10, new Color(0, 0, 0, 0)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        field.setOpaque(false);
        
        // Placeholder MÁS VISIBLE
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(TEXT_PRIMARY);
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(new Color(120, 120, 120)); // Más oscuro
                    field.setText(placeholder);
                }
            }
        });
        
        field.setText(placeholder);
        field.setForeground(new Color(120, 120, 120)); // Placeholder más visible
        
        return field;
    }
    
    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                if (!isOpaque() && getBorder() instanceof RoundedBorder) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2.dispose();
                }
                super.paintComponent(g);
            }
        };
        
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(TEXT_PRIMARY);
        field.setBackground(BACKGROUND_COLOR);
        field.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(10, new Color(0, 0, 0, 0)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        field.setOpaque(false);
        
        return field;
    }
    
    private void login() {
        String correoInput = txtCorreo.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        // Limpiar placeholder
        final String correo = correoInput.equals("usuario@ejemplo.com") ? "" : correoInput;
        
        if (correo.isEmpty() || password.isEmpty()) {
            showMessage("Por favor ingrese correo y contraseña", ERROR_COLOR);
            return;
        }
        
        // Proceso RÁPIDO sin delay innecesario
        btnLogin.setEnabled(false);
        btnLogin.setText("Verificando...");
        
        Usuario usuario = usuarioController.login(correo, password);
        
        if (usuario != null) {
            showMessage("Acceso concedido. Bienvenido " + usuario.getNombre(), SUCCESS_COLOR);
            dispose();
            new MenuPrincipalFrame(usuarioController).setVisible(true);
        } else {
            showMessage("Credenciales incorrectas", ERROR_COLOR);
            txtPassword.setText("");
            btnLogin.setEnabled(true);
            btnLogin.setText("Iniciar Sesión");
        }
    }
    
    private void showMessage(String message, Color color) {
        lblMensaje.setText(message);
        lblMensaje.setForeground(color);
    }
    
    // ==================== CLASES INTERNAS PARA DISEÑO MODERNO ====================
    
    /**
     * Botón moderno con efectos hover y sombra
     */
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
            
            setFont(new Font("Segoe UI", Font.BOLD, 14));
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
            
            // Determinar color según estado
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
            g2.setColor(new Color(0, 0, 0, 30));
            g2.fillRoundRect(2, 4, getWidth() - 4, getHeight() - 4, 12, 12);
            
            // Fondo del botón
            g2.setColor(currentColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight() - 4, 12, 12);
            
            g2.dispose();
            super.paintComponent(g);
        }
    }
    
    /**
     * Borde redondeado con sombra opcional
     */
    private static class RoundedBorder extends AbstractBorder {
        private int radius;
        private Color shadowColor;
        
        public RoundedBorder(int radius, Color shadowColor) {
            this.radius = radius;
            this.shadowColor = shadowColor;
        }
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Sombra
            if (shadowColor.getAlpha() > 0) {
                g2.setColor(shadowColor);
                g2.fillRoundRect(x + 2, y + 4, width - 4, height - 4, radius, radius);
            }
            
            g2.dispose();
        }
        
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius/2, radius/2, radius/2, radius/2);
        }
        
        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = insets.top = insets.bottom = radius/2;
            return insets;
        }
    }
}
