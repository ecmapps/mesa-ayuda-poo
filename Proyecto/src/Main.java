import Interfaz.InterfazDepartamento;
import Interfaz.InterfazGestionTicketsBow;
import Interfaz.InterfazTicket;
import Interfaz.InterfazUsuario;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    public Main() {
        configurarVentana();
        inicializarComponentes();
    }

    private void configurarVentana() {
        setTitle("Sistema de Gestión - Menú Principal");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
    }

    private void inicializarComponentes() {
        // Panel de título
        JPanel panelTitulo = new JPanel();
        JLabel lblTitulo = new JLabel("Sistema de Gestión");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        // Panel de botones
        JPanel panelBotones = new JPanel(new GridLayout(5, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Botón para Gestión de Departamentos
        JButton btnDepartamentos = new JButton("Gestión de Departamentos");
        btnDepartamentos.setFont(new Font("Arial", Font.PLAIN, 16));
        btnDepartamentos.addActionListener(e -> abrirInterfazDepartamento());

        // Botón para Gestión de Usuarios
        JButton btnUsuarios = new JButton("Gestión de Usuarios");
        btnUsuarios.setFont(new Font("Arial", Font.PLAIN, 16));
        btnUsuarios.addActionListener(e -> abrirInterfazUsuario());

        // Botón para Gestión de Tickets
        JButton btnTickets = new JButton("Gestión de Tickets");
        btnTickets.setFont(new Font("Arial", Font.PLAIN, 16));
        btnTickets.addActionListener(e -> abrirInterfazTicket());

        // Botón para Gestión de Tickets BOW
        JButton btnTicketsBow = new JButton("Gestión de Tickets BOW");
        btnTicketsBow.setFont(new Font("Arial", Font.PLAIN, 16));
        btnTicketsBow.addActionListener(e -> abrirInterfazGestionTicketsBow());

        // Botón para salir
        JButton btnSalir = new JButton("Salir");
        btnSalir.setFont(new Font("Arial", Font.PLAIN, 16));
        btnSalir.setBackground(new Color(220, 53, 69));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.addActionListener(e -> System.exit(0));

        panelBotones.add(btnDepartamentos);
        panelBotones.add(btnUsuarios);
        panelBotones.add(btnTickets);
        panelBotones.add(btnTicketsBow);
        panelBotones.add(btnSalir);

        add(panelBotones, BorderLayout.CENTER);

        // Panel de información
        JPanel panelInfo = new JPanel();
        JLabel lblInfo = new JLabel("Seleccione una opción del menú");
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 12));
        panelInfo.add(lblInfo);
        add(panelInfo, BorderLayout.SOUTH);
    }

    private void abrirInterfazDepartamento() {
        try {
            InterfazDepartamento interfaz = new InterfazDepartamento();
            interfaz.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al abrir Gestión de Departamentos: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirInterfazUsuario() {
        try {
            InterfazUsuario interfaz = new InterfazUsuario();
            interfaz.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al abrir Gestión de Usuarios: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirInterfazTicket() {
        try {
            InterfazTicket interfaz = new InterfazTicket();
            interfaz.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al abrir Gestión de Tickets: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirInterfazGestionTicketsBow() {
        try {
            InterfazGestionTicketsBow interfaz = new InterfazGestionTicketsBow();
            interfaz.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al abrir Gestión de Tickets BOW: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Establecer look and feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Ejecutar la aplicación en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            Main app = new Main();
            app.setVisible(true);
        });
    }
}