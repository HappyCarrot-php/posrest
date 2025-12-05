package com.restaurante.view;

import com.restaurante.config.SupabaseConfig;
import com.restaurante.controller.UsuarioController;
import com.restaurante.model.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Menú principal moderno y stylish con diseño basado en cards
 * Muestra opciones dinámicas según el rol del usuario
 */
public class MenuPrincipalFrame extends JFrame {
    
    private final UsuarioController usuarioController;
    private final Usuario usuarioActual;
    
    // Colores modernos
    private static final Color BACKGROUND = new Color(240, 242, 245);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(33, 37, 41);
    private static final Color TEXT_SECONDARY = new Color(108, 117, 125);
    
    // Colores para botones
    private static final Color VENTAS_COLOR = new Color(40, 167, 69);
    private static final Color PRODUCTOS_COLOR = new Color(0, 123, 255);
    private static final Color MESAS_COLOR = new Color(111, 66, 193);
    private static final Color USUARIOS_COLOR = new Color(253, 126, 20);
    private static final Color REPORTES_COLOR = new Color(220, 53, 69);
    private static final Color RESPALDO_COLOR = new Color(52, 152, 219);

    // Iconografía en formato Unicode para evitar problemas de codificación
    private static final String ICON_VENTAS = "\uD83D\uDED2"; // 🛒
    private static final String ICON_PRODUCTOS = "\uD83D\uDCE6"; // 📦
    private static final String ICON_MESAS = "\uD83C\uDF7D\uFE0F"; // 🍽️
    private static final String ICON_USUARIOS = "\uD83D\uDC65"; // 👥
    private static final String ICON_REPORTES = "\uD83D\uDCCA"; // 📊
    private static final String ICON_RESPALDO = "\uD83D\uDCBE"; // 💾
    
    public MenuPrincipalFrame(UsuarioController usuarioController) {
        this.usuarioController = usuarioController;
        this.usuarioActual = usuarioController.getUsuarioActual();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Sistema POS Restaurante - Dashboard");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        getContentPane().setBackground(BACKGROUND);
        
        // Layout principal
        setLayout(new BorderLayout(0, 0));
        
        // Header
        add(createHeader(), BorderLayout.NORTH);
        
        // Contenido principal
        add(createMainContent(), BorderLayout.CENTER);
        
        // Footer
        add(createFooter(), BorderLayout.SOUTH);
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        
        // Título y usuario
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        
        JLabel lblTitle = new JLabel("Dashboard POS");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(TEXT_PRIMARY);
        
        String rolText = getRolText(usuarioActual.getRol());
        JLabel lblUser = new JLabel(usuarioActual.getNombre() + " • " + rolText);
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUser.setForeground(TEXT_SECONDARY);
        
        leftPanel.add(lblTitle);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        leftPanel.add(lblUser);
        
        // Botón cerrar sesión
        ModernButton btnLogout = new ModernButton("Cerrar Sesión", new Color(220, 53, 69), Color.WHITE);
        btnLogout.setPreferredSize(new Dimension(140, 40));
        btnLogout.addActionListener(e -> cerrarSesion());
        
        header.add(leftPanel, BorderLayout.WEST);
        header.add(btnLogout, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createMainContent() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        int row = 0;
        int col = 0;
        int cardsPerRow = 3;
        
        // Card: Registrar Venta (todos los roles)
        gbc.gridx = col++;
        gbc.gridy = row;
        mainPanel.add(createMenuCard(
            ICON_VENTAS, "Nueva Venta", "Registrar venta de productos", VENTAS_COLOR,
            () -> new VentaFrame(usuarioController).setVisible(true)
        ), gbc);
        
        // Card: Productos (cajero y administrador)
        if (usuarioController.esCajero() || usuarioController.esAdministrador()) {
            gbc.gridx = col++;
            gbc.gridy = row;
            mainPanel.add(createMenuCard(
                ICON_PRODUCTOS, "Productos", "Gestionar inventario de productos", PRODUCTOS_COLOR,
                () -> new ProductoFrame().setVisible(true)
            ), gbc);
        }
        
        // Card: Mesas (todos)
        if (col >= cardsPerRow) {
            row++;
            col = 0;
        }
        gbc.gridx = col++;
        gbc.gridy = row;
        mainPanel.add(createMenuCard(
            ICON_MESAS, "Mesas", "Administrar mesas del restaurante", MESAS_COLOR,
            () -> new MesaFrame().setVisible(true)
        ), gbc);
        
        // Card: Usuarios (solo administrador)
        if (usuarioController.esAdministrador()) {
            if (col >= cardsPerRow) {
                row++;
                col = 0;
            }
            gbc.gridx = col++;
            gbc.gridy = row;
            mainPanel.add(createMenuCard(
                ICON_USUARIOS, "Usuarios", "Gestionar usuarios del sistema", USUARIOS_COLOR,
                () -> new UsuarioFrame().setVisible(true)
            ), gbc);
        }
        
        // Card: Reportes (administrador y cajero)
        if (usuarioController.esAdministrador() || usuarioController.esCajero()) {
            if (col >= cardsPerRow) {
                row++;
                col = 0;
            }
            gbc.gridx = col++;
            gbc.gridy = row;
            mainPanel.add(createMenuCard(
                ICON_REPORTES, "Reportes", "Ver estadísticas y reportes", REPORTES_COLOR,
                () -> new ReporteFrame().setVisible(true)
            ), gbc);
        }
        
        // Card: Respaldo BD (solo administrador)
        if (usuarioController.esAdministrador()) {
            if (col >= cardsPerRow) {
                row++;
                col = 0;
            }
            gbc.gridx = col++;
            gbc.gridy = row;
            mainPanel.add(createMenuCard(
                ICON_RESPALDO, "Respaldo BD", "Exportar base de datos SQL", RESPALDO_COLOR,
                () -> exportarRespaldoBD()
            ), gbc);
        }
        
        // Relleno para distribuir espacio
        gbc.gridx = 0;
        gbc.gridy = row + 1;
        gbc.gridwidth = cardsPerRow;
        gbc.weighty = 0.5;
        mainPanel.add(Box.createVerticalGlue(), gbc);
        
        return mainPanel;
    }
    
    private JPanel createMenuCard(String icon, String title, String description, Color accentColor, Runnable action) {
        final JPanel accentLine = new JPanel();
        accentLine.setBackground(accentColor);
        accentLine.setPreferredSize(new Dimension(6, 56));

        JPanel card = new JPanel() {
            private boolean isHovered = false;

            {
                setToolTipText(description);
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        isHovered = true;
                        setCursor(new Cursor(Cursor.HAND_CURSOR));
                        accentLine.setBackground(accentColor.brighter());
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        isHovered = false;
                        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        accentLine.setBackground(accentColor);
                        repaint();
                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        action.run();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color shadow = isHovered ? new Color(0, 0, 0, 45) : new Color(0, 0, 0, 20);
                g2.setColor(shadow);
                g2.fill(new RoundRectangle2D.Double(4, 6, getWidth() - 8, getHeight() - 12, 18, 18));

                g2.setColor(CARD_BG);
                g2.fill(new RoundRectangle2D.Double(6, 6, getWidth() - 12, getHeight() - 12, 18, 18));

                g2.dispose();
                super.paintComponent(g);
            }
        };

        card.setLayout(new BorderLayout(0, 18));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(30, 28, 30, 28));
        card.setPreferredSize(new Dimension(280, 190));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 56));
        lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
        lblIcon.setForeground(accentColor.darker());
        lblIcon.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 0));

        topPanel.add(accentLine, BorderLayout.WEST);
        topPanel.add(lblIcon, BorderLayout.CENTER);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(TEXT_PRIMARY);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDesc = new JLabel("<html>" + description + "</html>");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDesc.setForeground(TEXT_SECONDARY);
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(lblTitle);
        textPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        textPanel.add(lblDesc);

        card.add(topPanel, BorderLayout.NORTH);
        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }
    
    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(Color.WHITE);
        footer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel lblFooter = new JLabel("Sistema POS Restaurante v1.0 • © 2025");
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFooter.setForeground(TEXT_SECONDARY);
        
        footer.add(lblFooter);
        
        return footer;
    }
    
    private String getRolText(String rol) {
        switch (rol.toLowerCase()) {
            case "administrador": return "Administrador";
            case "cajero": return "Cajero";
            case "mesero": return "Mesero";
            default: return rol;
        }
    }
    
    private void exportarRespaldoBD() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Respaldo de Base de Datos");
        fileChooser.setSelectedFile(new java.io.File("respaldo_pos_" + 
            java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".sql"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().toLowerCase().endsWith(".sql")) {
                fileToSave = new java.io.File(fileToSave.getAbsolutePath() + ".sql");
            }

            try {
                String projectId = SupabaseConfig.SUPABASE_URL
                    .replace("https://", "")
                    .replace(".supabase.co", "");
                String dbHost = "db." + projectId + ".supabase.co";
                String dbPort = "5432";
                String dbName = "postgres";
                String dbUser = SupabaseConfig.getDbUser();
                String dbPassword = SupabaseConfig.getDbPassword();

                if (dbPassword == null || dbPassword.isBlank()) {
                    JOptionPane.showMessageDialog(this,
                        "No se encontró la contraseña en SupabaseConfig.getDbPassword().\n" +
                        "Actualiza el archivo con tus credenciales reales.",
                        "Credenciales incompletas",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String command = String.format(
                    "pg_dump -h %s -p %s -U %s -d %s -f \"%s\"",
                    dbHost, dbPort, dbUser, dbName, fileToSave.getAbsolutePath()
                );

                ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", command);
                pb.environment().put("PGPASSWORD", dbPassword);
                pb.redirectErrorStream(true);

                Process process = pb.start();
                int exitCode = process.waitFor();

                if (exitCode == 0) {
                    JOptionPane.showMessageDialog(this,
                        "Respaldo creado exitosamente en:\n" + fileToSave.getAbsolutePath(),
                        "Respaldo Completado",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Error al crear respaldo. Asegúrese de tener PostgreSQL instalado y en PATH.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Error al crear respaldo:\n" + ex.getMessage() +
                    "\n\nAsegúrese de tener pg_dump instalado y accesible.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void cerrarSesion() {
        int opcion = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro de cerrar sesión?",
            "Confirmar cierre de sesión",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (opcion == JOptionPane.YES_OPTION) {
            usuarioController.logout();
            SwingUtilities.invokeLater(() -> {
                new LoginFrame().setVisible(true);
                dispose();
            });
        }
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
