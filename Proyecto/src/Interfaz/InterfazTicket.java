package Interfaz;

import AccesoADatos.*;
import diccionario.AnalisisBow;
import LogicaDeNegocio.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Collection;

public class InterfazTicket extends JFrame {

    private ServicioTicket servicioTicket;
    private ServicioUsuario servicioUsuario;
    private ServicioDepartamento ServicioDepartamento;
    private ServicioDiccionarioEmocional servicioDiccionarioEmocional;
    private ServicioDiccionarioTecnico servicioDiccionarioTecnico;

    // Componentes
    private JTextField txtId, txtAsunto, txtCedulaUsuario;
    private JTextArea txtDescripcion;
    private JComboBox<Estado> cmbEstado;
    private JComboBox<String> cmbDepartamento;
    private JTable tablaTickets;
    private DefaultTableModel modeloTabla;
    private JButton btnCrear, btnActualizarEstado, btnBuscar, btnLimpiar, btnListar;

    public InterfazTicket() {
        servicioTicket = new ServicioTicket();
        servicioUsuario = new ServicioUsuario();
        ServicioDepartamento = new ServicioDepartamento();
        servicioDiccionarioEmocional = new ServicioDiccionarioEmocional();
        servicioDiccionarioTecnico = new ServicioDiccionarioTecnico();
        inicializarComponentes();
        configurarVentana();
        cargarTickets();
        cargarDepartamentos();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelFormulario = crearPanelFormulario();
        add(panelFormulario, BorderLayout.NORTH);

        JPanel panelBotones = crearPanelBotones();
        add(panelBotones, BorderLayout.CENTER);

        JPanel panelTabla = crearPanelTabla();
        add(panelTabla, BorderLayout.SOUTH);
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Ticket"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ID
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("ID Ticket:"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField(20);
        panel.add(txtId, gbc);

        // Asunto
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Asunto:"), gbc);
        gbc.gridx = 1;
        txtAsunto = new JTextField(20);
        panel.add(txtAsunto, gbc);

        // Descripción
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.NORTH;
        panel.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        txtDescripcion = new JTextArea(4, 20);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);
        panel.add(scrollDescripcion, gbc);

        // Estado
        gbc.gridx = 2; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;
        gbc.weighty = 0;
        panel.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 3;
        cmbEstado = new JComboBox<>(Estado.values());
        panel.add(cmbEstado, gbc);

        // Cédula Usuario
        gbc.gridx = 2; gbc.gridy = 1;
        panel.add(new JLabel("Cédula Usuario:"), gbc);
        gbc.gridx = 3;
        txtCedulaUsuario = new JTextField(20);
        panel.add(txtCedulaUsuario, gbc);

        // Departamento
        gbc.gridx = 2; gbc.gridy = 2;
        panel.add(new JLabel("Departamento:"), gbc);
        gbc.gridx = 3;
        cmbDepartamento = new JComboBox<>();
        panel.add(cmbDepartamento, gbc);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        btnCrear = new JButton("Crear Ticket");
        btnCrear.addActionListener(e -> crearTicket());

        btnActualizarEstado = new JButton("Actualizar Estado");
        btnActualizarEstado.addActionListener(e -> actualizarEstado());

        btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscarTicket());

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(e -> limpiarCampos());

        btnListar = new JButton("Refrescar Lista");
        btnListar.addActionListener(e -> cargarTickets());

        panel.add(btnCrear);
        panel.add(btnActualizarEstado);
        panel.add(btnBuscar);
        panel.add(btnLimpiar);
        panel.add(btnListar);

        return panel;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Lista de Tickets"));

        String[] columnas = {"ID", "Asunto", "Descripción", "Estado",
                "Usuario", "Departamento"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaTickets = new JTable(modeloTabla);
        tablaTickets.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaTickets.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 1) {
                    cargarTicketSeleccionado();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaTickets);
        scrollPane.setPreferredSize(new Dimension(900, 200));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void crearTicket() {
        try {
            if (validarCampos()) {
                Ticket ticket = crearTicketDesdeFormulario();
                servicioTicket.crearTicket(ticket);
                
                // Inicia analisis BOW
                AnalisisBow analisisBow = new AnalisisBow(
                    servicioDiccionarioEmocional,
                    servicioDiccionarioTecnico
                );
                analisisBow.analizarPalabras(ticket);
                
                String estadoAnimo = analisisBow.getEstadoAnimo();
                String categoria = analisisBow.getCategoriaSugerida();
                
                // Guardar análisis en BD
                ServicioAnalisisBow servicioAnalisis = new ServicioAnalisisBow();
                servicioAnalisis.insertarAnalisis(ticket.getId(), estadoAnimo, categoria);
                
                JOptionPane.showMessageDialog(this,
                        "Ticket creado exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                cargarTickets();
            }
        } catch (GlobalException | NoDataException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarEstado() {
        try {
            String id = txtId.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Debe ingresar un ID de ticket",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Estado nuevoEstado = (Estado) cmbEstado.getSelectedItem();
            servicioTicket.actualizarEstado(id, nuevoEstado);
            JOptionPane.showMessageDialog(this,
                    "Estado actualizado exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            cargarTickets();

        } catch (GlobalException | NoDataException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarTicket() {
        try {
            String id = txtId.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Debe ingresar un ID",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Ticket ticket = servicioTicket.buscarTicket(id);
            cargarTicketEnFormulario(ticket);

        } catch (GlobalException | NoDataException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTickets() {
        try {
            modeloTabla.setRowCount(0);
            Collection<Ticket> tickets = servicioTicket.listarTickets();

            for (Ticket t : tickets) {
                Object[] fila = {
                        t.getId(),
                        t.getAsunto(),
                        t.getDescripcion(),
                        t.getEstado(),
                        t.getUsuario() != null ? t.getUsuario().getCedula() : "N/A",
                        t.getDepartamento() != null ? t.getDepartamento().getId() : "N/A"
                };
                modeloTabla.addRow(fila);
            }
        } catch (GlobalException | NoDataException ex) {
            // Si no hay datos, simplemente no mostrar nada
        }
    }

    private void cargarDepartamentos() {
        try {
            cmbDepartamento.removeAllItems();
            Collection<Departamento> departamentos = ServicioDepartamento.listarDepartamentos();

            for (Departamento d : departamentos) {
                cmbDepartamento.addItem(d.getId() + " - " + d.getNombre());
            }
        } catch (GlobalException | NoDataException ex) {
            // Si no hay departamentos disponibles
        }
    }

    private void cargarTicketSeleccionado() {
        int filaSeleccionada = tablaTickets.getSelectedRow();
        if (filaSeleccionada != -1) {
            txtId.setText(modeloTabla.getValueAt(filaSeleccionada, 0).toString());
            txtAsunto.setText(modeloTabla.getValueAt(filaSeleccionada, 1).toString());
            txtDescripcion.setText(modeloTabla.getValueAt(filaSeleccionada, 2).toString());
            cmbEstado.setSelectedItem(Estado.valueOf(modeloTabla.getValueAt(filaSeleccionada, 3).toString()));
            txtCedulaUsuario.setText(modeloTabla.getValueAt(filaSeleccionada, 4).toString());

            // Buscar el departamento en el combo
            String deptId = modeloTabla.getValueAt(filaSeleccionada, 5).toString();
            for (int i = 0; i < cmbDepartamento.getItemCount(); i++) {
                if (cmbDepartamento.getItemAt(i).startsWith(deptId)) {
                    cmbDepartamento.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void cargarTicketEnFormulario(Ticket t) {
        txtId.setText(t.getId());
        txtAsunto.setText(t.getAsunto());
        txtDescripcion.setText(t.getDescripcion());
        cmbEstado.setSelectedItem(t.getEstado());
        txtCedulaUsuario.setText(t.getUsuario() != null ? t.getUsuario().getCedula() : "");

        // Buscar el departamento en el combo
        if (t.getDepartamento() != null) {
            String deptId = t.getDepartamento().getId();
            for (int i = 0; i < cmbDepartamento.getItemCount(); i++) {
                if (cmbDepartamento.getItemAt(i).startsWith(deptId)) {
                    cmbDepartamento.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private Ticket crearTicketDesdeFormulario() {
        Usuario usuario = new Usuario();
        usuario.setCedula(txtCedulaUsuario.getText().trim());

        Departamento departamento = new Departamento();
        String deptSeleccionado = (String) cmbDepartamento.getSelectedItem();
        String deptId = deptSeleccionado.split(" - ")[0];
        departamento.setId(deptId);

        return new Ticket(
                txtId.getText().trim(),
                txtAsunto.getText().trim(),
                txtDescripcion.getText().trim(),
                (Estado) cmbEstado.getSelectedItem(),
                usuario,
                departamento
        );
    }

    private boolean validarCampos() {
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El ID es obligatorio",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (txtAsunto.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El asunto es obligatorio",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (txtCedulaUsuario.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La cédula del usuario es obligatoria",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (cmbDepartamento.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un departamento",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtAsunto.setText("");
        txtDescripcion.setText("");
        txtCedulaUsuario.setText("");
        cmbEstado.setSelectedIndex(0);
        if (cmbDepartamento.getItemCount() > 0) {
            cmbDepartamento.setSelectedIndex(0);
        }
    }

    private void configurarVentana() {
        setTitle("Gestión de Tickets");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InterfazTicket().setVisible(true);
        });
    }
}