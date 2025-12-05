package Interfaz;

import Controlador.ControladorUsuario;
import AccesoADatos.*;
import LogicaDeNegocio.Usuario;
import LogicaDeNegocio.Rol;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Collection;

public class InterfazUsuario extends JFrame {

    private ControladorUsuario controladorUsuario;

    // Componentes
    private JTextField txtCedula, txtNombre, txtApellido1, txtApellido2;
    private JTextField txtCorreo, txtContrasenna, txtTelefono;
    private JComboBox<Rol> cmbRol;
    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;
    private JButton btnRegistrar, btnActualizar, btnEliminar, btnBuscar, btnLimpiar, btnListar;

    public InterfazUsuario() {
        controladorUsuario = new ControladorUsuario();
        inicializarComponentes();
        configurarVentana();
        cargarUsuarios();
    }

    private void inicializarComponentes() {
        // Panel principal con BorderLayout
        setLayout(new BorderLayout(10, 10));

        // Panel de formulario
        JPanel panelFormulario = crearPanelFormulario();
        add(panelFormulario, BorderLayout.NORTH);

        // Panel de botones
        JPanel panelBotones = crearPanelBotones();
        add(panelBotones, BorderLayout.CENTER);

        // Panel de tabla
        JPanel panelTabla = crearPanelTabla();
        add(panelTabla, BorderLayout.SOUTH);
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Usuario"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Cédula
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Cédula:"), gbc);
        gbc.gridx = 1;
        txtCedula = new JTextField(20);
        panel.add(txtCedula, gbc);

        // Nombre
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField(20);
        panel.add(txtNombre, gbc);

        // Apellido 1
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Apellido 1:"), gbc);
        gbc.gridx = 1;
        txtApellido1 = new JTextField(20);
        panel.add(txtApellido1, gbc);

        // Apellido 2
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Apellido 2:"), gbc);
        gbc.gridx = 1;
        txtApellido2 = new JTextField(20);
        panel.add(txtApellido2, gbc);

        // Correo
        gbc.gridx = 2; gbc.gridy = 0;
        panel.add(new JLabel("Correo:"), gbc);
        gbc.gridx = 3;
        txtCorreo = new JTextField(20);
        panel.add(txtCorreo, gbc);

        // Contraseña
        gbc.gridx = 2; gbc.gridy = 1;
        panel.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 3;
        txtContrasenna = new JPasswordField(20);
        panel.add(txtContrasenna, gbc);

        // Teléfono
        gbc.gridx = 2; gbc.gridy = 2;
        panel.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 3;
        txtTelefono = new JTextField(20);
        panel.add(txtTelefono, gbc);

        // Rol
        gbc.gridx = 2; gbc.gridy = 3;
        panel.add(new JLabel("Rol:"), gbc);
        gbc.gridx = 3;
        cmbRol = new JComboBox<>(Rol.values());
        panel.add(cmbRol, gbc);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        btnRegistrar = new JButton("Registrar");
        btnRegistrar.addActionListener(e -> registrarUsuario());

        btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> actualizarUsuario());

        btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(e -> eliminarUsuario());

        btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscarUsuario());

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(e -> limpiarCampos());

        btnListar = new JButton("Refrescar Lista");
        btnListar.addActionListener(e -> cargarUsuarios());

        panel.add(btnRegistrar);
        panel.add(btnActualizar);
        panel.add(btnEliminar);
        panel.add(btnBuscar);
        panel.add(btnLimpiar);
        panel.add(btnListar);

        return panel;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Lista de Usuarios"));

        String[] columnas = {"Cédula", "Nombre", "Apellido 1", "Apellido 2",
                "Correo", "Teléfono", "Rol"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 1) {
                    cargarUsuarioSeleccionado();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaUsuarios);
        scrollPane.setPreferredSize(new Dimension(800, 200));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void registrarUsuario() {
        try {
            if (validarCampos()) {
                controladorUsuario.registrarUsuario(
                        txtCedula.getText().trim(),
                        txtNombre.getText().trim(),
                        txtApellido1.getText().trim(),
                        txtApellido2.getText().trim(),
                        txtCorreo.getText().trim(),
                        txtContrasenna.getText().trim(),
                        txtTelefono.getText().trim(),
                        (Rol) cmbRol.getSelectedItem()
                );
                JOptionPane.showMessageDialog(this,
                        "Usuario registrado exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                cargarUsuarios();
            }
        } catch (GlobalException | NoDataException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarUsuario() {
        try {
            if (validarCampos()) {
                controladorUsuario.actualizarUsuario(
                        txtCedula.getText().trim(),
                        txtNombre.getText().trim(),
                        txtApellido1.getText().trim(),
                        txtApellido2.getText().trim(),
                        txtCorreo.getText().trim(),
                        txtContrasenna.getText().trim(),
                        txtTelefono.getText().trim(),
                        (Rol) cmbRol.getSelectedItem()
                );
                JOptionPane.showMessageDialog(this,
                        "Usuario actualizado exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                cargarUsuarios();
            }
        } catch (GlobalException | NoDataException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarUsuario() {
        try {
            String cedula = txtCedula.getText().trim();
            if (cedula.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Debe ingresar una cédula",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de eliminar este usuario?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                controladorUsuario.eliminarUsuario(cedula);
                JOptionPane.showMessageDialog(this,
                        "Usuario eliminado exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                cargarUsuarios();
            }
        } catch (GlobalException | NoDataException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarUsuario() {
        try {
            String cedula = txtCedula.getText().trim();
            if (cedula.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Debe ingresar una cédula",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Usuario usuario = controladorUsuario.buscarUsuario(cedula);
            cargarUsuarioEnFormulario(usuario);

        } catch (GlobalException | NoDataException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarUsuarios() {
        try {
            modeloTabla.setRowCount(0);
            Collection<Usuario> usuarios = controladorUsuario.listarUsuarios();

            for (Usuario u : usuarios) {
                Object[] fila = {
                        u.getCedula(),
                        u.getNombre(),
                        u.getApellido1(),
                        u.getApellido2(),
                        u.getCorreoElectronico(),
                        u.getNumTelefono(),
                        u.getRol()
                };
                modeloTabla.addRow(fila);
            }
        } catch (GlobalException | NoDataException ex) {
            // Si no hay datos, simplemente no mostrar nada
        }
    }

    private void cargarUsuarioSeleccionado() {
        int filaSeleccionada = tablaUsuarios.getSelectedRow();
        if (filaSeleccionada != -1) {
            txtCedula.setText(modeloTabla.getValueAt(filaSeleccionada, 0).toString());
            txtNombre.setText(modeloTabla.getValueAt(filaSeleccionada, 1).toString());
            txtApellido1.setText(modeloTabla.getValueAt(filaSeleccionada, 2).toString());
            txtApellido2.setText(modeloTabla.getValueAt(filaSeleccionada, 3).toString());
            txtCorreo.setText(modeloTabla.getValueAt(filaSeleccionada, 4).toString());
            txtTelefono.setText(modeloTabla.getValueAt(filaSeleccionada, 5).toString());
            cmbRol.setSelectedItem(Rol.valueOf(modeloTabla.getValueAt(filaSeleccionada, 6).toString()));
        }
    }

    private void cargarUsuarioEnFormulario(Usuario u) {
        txtCedula.setText(u.getCedula());
        txtNombre.setText(u.getNombre());
        txtApellido1.setText(u.getApellido1());
        txtApellido2.setText(u.getApellido2());
        txtCorreo.setText(u.getCorreoElectronico());
        txtContrasenna.setText(u.getContrasenna());
        txtTelefono.setText(u.getNumTelefono());
        cmbRol.setSelectedItem(u.getRol());
    }

    private Usuario crearUsuarioDesdeFormulario() {
        return new Usuario(
                txtCedula.getText().trim(),
                txtNombre.getText().trim(),
                txtApellido1.getText().trim(),
                txtApellido2.getText().trim(),
                txtCorreo.getText().trim(),
                txtContrasenna.getText().trim(),
                txtTelefono.getText().trim(),
                (Rol) cmbRol.getSelectedItem()
        );
    }

    private boolean validarCampos() {
        if (txtCedula.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La cédula es obligatoria",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (txtCorreo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El correo es obligatorio",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void limpiarCampos() {
        txtCedula.setText("");
        txtNombre.setText("");
        txtApellido1.setText("");
        txtApellido2.setText("");
        txtCorreo.setText("");
        txtContrasenna.setText("");
        txtTelefono.setText("");
        cmbRol.setSelectedIndex(0);
    }

    private void configurarVentana() {
        setTitle("Gestión de Usuarios");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InterfazUsuario().setVisible(true);
        });
    }
}