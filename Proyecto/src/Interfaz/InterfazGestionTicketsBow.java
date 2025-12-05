package Interfaz;

import AccesoADatos.*;
import LogicaDeNegocio.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Collection;

public class InterfazGestionTicketsBow extends JFrame {

    private ServicioTicket servicioTicket;
    private ServicioUsuario servicioUsuario;
    private ServicioDepartamento servicioDepartamento;

    // Componentes
    private JTabbedPane tabbedPane;
    private JTextField txtCedulaBusqueda, txtDepartamentoBusqueda;
    private JTable tablaTicketsUsuario, tablaTicketsDepartamento, tablaTodosTickets;
    private DefaultTableModel modeloTicketsUsuario, modeloTicketsDepartamento, modeloTodosTickets;
    private JButton btnBuscarPorUsuario, btnBuscarPorDepartamento, btnRefrescarTodos;

    public InterfazGestionTicketsBow() {
        servicioTicket = new ServicioTicket();
        servicioUsuario = new ServicioUsuario();
        servicioDepartamento = new ServicioDepartamento();
        inicializarComponentes();
        configurarVentana();
        cargarTodosLosTickets();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));

        // Crear panel de pestañas
        tabbedPane = new JTabbedPane();

        // Pestaña 1: Consultar por Usuario
        JPanel panelUsuario = crearPanelConsultaUsuario();
        tabbedPane.addTab("Tickets por Usuario", panelUsuario);

        // Pestaña 2: Consultar por Departamento
        JPanel panelDepartamento = crearPanelConsultaDepartamento();
        tabbedPane.addTab("Tickets por Departamento", panelDepartamento);

        // Pestaña 3: Todos los Tickets
        JPanel panelTodos = crearPanelTodosTickets();
        tabbedPane.addTab("Todos los Tickets", panelTodos);

        add(tabbedPane, BorderLayout.CENTER);

        // Panel de estadísticas
        JPanel panelEstadisticas = crearPanelEstadisticas();
        add(panelEstadisticas, BorderLayout.SOUTH);
    }

    private JPanel crearPanelConsultaUsuario() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.add(new JLabel("Cédula del Usuario:"));
        txtCedulaBusqueda = new JTextField(20);
        panelBusqueda.add(txtCedulaBusqueda);
        btnBuscarPorUsuario = new JButton("Buscar Tickets");
        btnBuscarPorUsuario.addActionListener(e -> buscarTicketsPorUsuario());
        panelBusqueda.add(btnBuscarPorUsuario);

        panel.add(panelBusqueda, BorderLayout.NORTH);

        // Tabla de resultados
        String[] columnas = {"ID", "Asunto", "Descripción", "Estado", "Departamento"};
        modeloTicketsUsuario = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaTicketsUsuario = new JTable(modeloTicketsUsuario);
        tablaTicketsUsuario.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tablaTicketsUsuario);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelConsultaDepartamento() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.add(new JLabel("ID del Departamento:"));
        txtDepartamentoBusqueda = new JTextField(20);
        panelBusqueda.add(txtDepartamentoBusqueda);
        btnBuscarPorDepartamento = new JButton("Buscar Tickets");
        btnBuscarPorDepartamento.addActionListener(e -> buscarTicketsPorDepartamento());
        panelBusqueda.add(btnBuscarPorDepartamento);

        panel.add(panelBusqueda, BorderLayout.NORTH);

        // Tabla de resultados
        String[] columnas = {"ID", "Asunto", "Descripción", "Estado", "Usuario"};
        modeloTicketsDepartamento = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaTicketsDepartamento = new JTable(modeloTicketsDepartamento);
        tablaTicketsDepartamento.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tablaTicketsDepartamento);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelTodosTickets() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de control
        JPanel panelControl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnRefrescarTodos = new JButton("Refrescar Lista");
        btnRefrescarTodos.addActionListener(e -> cargarTodosLosTickets());
        panelControl.add(btnRefrescarTodos);

        JButton btnVerDetalle = new JButton("Ver Detalle");
        btnVerDetalle.addActionListener(e -> verDetalleTicket());
        panelControl.add(btnVerDetalle);

        panel.add(panelControl, BorderLayout.NORTH);

        // Tabla de resultados
        String[] columnas = {"ID", "Asunto", "Descripción", "Estado", "Usuario", "Departamento"};
        modeloTodosTickets = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaTodosTickets = new JTable(modeloTodosTickets);
        tablaTodosTickets.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tablaTodosTickets);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelEstadisticas() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Estadísticas"));
        panel.setPreferredSize(new Dimension(0, 80));

        JLabel lblTotalTickets = new JLabel("Total Tickets: 0", SwingConstants.CENTER);
        JLabel lblNuevos = new JLabel("Nuevos: 0", SwingConstants.CENTER);
        JLabel lblEnProgreso = new JLabel("En Progreso: 0", SwingConstants.CENTER);
        JLabel lblResueltos = new JLabel("Resueltos: 0", SwingConstants.CENTER);

        lblTotalTickets.setFont(new Font("Arial", Font.BOLD, 14));
        lblNuevos.setFont(new Font("Arial", Font.BOLD, 14));
        lblEnProgreso.setFont(new Font("Arial", Font.BOLD, 14));
        lblResueltos.setFont(new Font("Arial", Font.BOLD, 14));

        panel.add(lblTotalTickets);
        panel.add(lblNuevos);
        panel.add(lblEnProgreso);
        panel.add(lblResueltos);

        // Guardar referencias para actualizar
        panel.putClientProperty("lblTotal", lblTotalTickets);
        panel.putClientProperty("lblNuevos", lblNuevos);
        panel.putClientProperty("lblEnProgreso", lblEnProgreso);
        panel.putClientProperty("lblResueltos", lblResueltos);

        return panel;
    }

    private void buscarTicketsPorUsuario() {
        try {
            String cedula = txtCedulaBusqueda.getText().trim();
            if (cedula.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Debe ingresar una cédula",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            modeloTicketsUsuario.setRowCount(0);
            Collection<Ticket> tickets = servicioTicket.consultarTicketsUsuario(cedula);

            for (Ticket t : tickets) {
                Object[] fila = {
                        t.getId(),
                        t.getAsunto(),
                        t.getDescripcion(),
                        t.getEstado(),
                        t.getDepartamento() != null ? t.getDepartamento().getId() : "N/A"
                };
                modeloTicketsUsuario.addRow(fila);
            }

            if (tickets.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No se encontraron tickets para este usuario",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (GlobalException | NoDataException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarTicketsPorDepartamento() {
        try {
            String departamentoId = txtDepartamentoBusqueda.getText().trim();
            if (departamentoId.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Debe ingresar un ID de departamento",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            modeloTicketsDepartamento.setRowCount(0);
            Collection<Ticket> tickets = servicioTicket.consultarTicketsDepartamento(departamentoId);

            for (Ticket t : tickets) {
                Object[] fila = {
                        t.getId(),
                        t.getAsunto(),
                        t.getDescripcion(),
                        t.getEstado(),
                        t.getUsuario() != null ? t.getUsuario().getCedula() : "N/A"
                };
                modeloTicketsDepartamento.addRow(fila);
            }

            if (tickets.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No se encontraron tickets para este departamento",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (GlobalException | NoDataException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTodosLosTickets() {
        try {
            modeloTodosTickets.setRowCount(0);
            Collection<Ticket> tickets = servicioTicket.listarTickets();

            int nuevos = 0, enProgreso = 0, resueltos = 0;

            for (Ticket t : tickets) {
                Object[] fila = {
                        t.getId(),
                        t.getAsunto(),
                        t.getDescripcion(),
                        t.getEstado(),
                        t.getUsuario() != null ? t.getUsuario().getCedula() : "N/A",
                        t.getDepartamento() != null ? t.getDepartamento().getId() : "N/A"
                };
                modeloTodosTickets.addRow(fila);

                // Contar por estado
                if (t.getEstado() == Estado.NUEVO) nuevos++;
                else if (t.getEstado() == Estado.EN_PROGRESO) enProgreso++;
                else if (t.getEstado() == Estado.RESUELTO) resueltos++;
            }

            // Actualizar estadísticas
            actualizarEstadisticas(tickets.size(), nuevos, enProgreso, resueltos);

        } catch (GlobalException | NoDataException ex) {
            // Si no hay datos, limpiar estadísticas
            actualizarEstadisticas(0, 0, 0, 0);
        }
    }

    private void actualizarEstadisticas(int total, int nuevos, int enProgreso, int resueltos) {
        Component[] components = ((JPanel) getContentPane().getComponent(1)).getComponents();
        JPanel panelStats = (JPanel) getContentPane().getComponent(1);

        JLabel lblTotal = (JLabel) panelStats.getClientProperty("lblTotal");
        JLabel lblNuevos = (JLabel) panelStats.getClientProperty("lblNuevos");
        JLabel lblEnProgreso = (JLabel) panelStats.getClientProperty("lblEnProgreso");
        JLabel lblResueltos = (JLabel) panelStats.getClientProperty("lblResueltos");

        if (lblTotal != null) lblTotal.setText("Total Tickets: " + total);
        if (lblNuevos != null) lblNuevos.setText("Nuevos: " + nuevos);
        if (lblEnProgreso != null) lblEnProgreso.setText("En Progreso: " + enProgreso);
        if (lblResueltos != null) lblResueltos.setText("Resueltos: " + resueltos);
    }

    private void verDetalleTicket() {
        int filaSeleccionada = tablaTodosTickets.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un ticket",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = modeloTodosTickets.getValueAt(filaSeleccionada, 0).toString();
        String asunto = modeloTodosTickets.getValueAt(filaSeleccionada, 1).toString();
        String descripcion = modeloTodosTickets.getValueAt(filaSeleccionada, 2).toString();
        String estado = modeloTodosTickets.getValueAt(filaSeleccionada, 3).toString();
        String usuario = modeloTodosTickets.getValueAt(filaSeleccionada, 4).toString();
        String departamento = modeloTodosTickets.getValueAt(filaSeleccionada, 5).toString();

        String detalle = String.format(
                "ID: %s\nAsunto: %s\nDescripción: %s\nEstado: %s\nUsuario: %s\nDepartamento: %s",
                id, asunto, descripcion, estado, usuario, departamento
        );

        JOptionPane.showMessageDialog(this,
                detalle,
                "Detalle del Ticket",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void configurarVentana() {
        setTitle("Sistema de Gestión de Tickets - Bow");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear menú
        JMenuBar menuBar = new JMenuBar();

        JMenu menuGestion = new JMenu("Gestión");
        JMenuItem itemUsuarios = new JMenuItem("Usuarios");
        itemUsuarios.addActionListener(e -> new InterfazUsuario().setVisible(true));
        JMenuItem itemDepartamentos = new JMenuItem("Departamentos");
        itemDepartamentos.addActionListener(e -> new InterfazDepartamento().setVisible(true));
        JMenuItem itemTickets = new JMenuItem("Tickets");
        itemTickets.addActionListener(e -> new InterfazTicket().setVisible(true));

        menuGestion.add(itemUsuarios);
        menuGestion.add(itemDepartamentos);
        menuGestion.add(itemTickets);

        JMenu menuAyuda = new JMenu("Ayuda");
        JMenuItem itemAcerca = new JMenuItem("Acerca de");
        itemAcerca.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Sistema de Gestión de Tickets Bow\nVersión 1.0",
                        "Acerca de",
                        JOptionPane.INFORMATION_MESSAGE)
        );
        menuAyuda.add(itemAcerca);

        menuBar.add(menuGestion);
        menuBar.add(menuAyuda);

        setJMenuBar(menuBar);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InterfazGestionTicketsBow().setVisible(true);
        });
    }
}