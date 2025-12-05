package Interfaz;

import Controlador.ControladorDepartamento;
import AccesoADatos.*;
import LogicaDeNegocio.Departamento;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Collection;

public class InterfazDepartamento extends JFrame {

    private ControladorDepartamento controladorDepartamento;

    // Componentes
    private JTextField txtId, txtNombre;
    private JTable tablaDepartamentos;
    private DefaultTableModel modeloTabla;
    private JButton btnRegistrar, btnActualizar, btnEliminar, btnBuscar, btnLimpiar, btnListar;

    public InterfazDepartamento() {
        controladorDepartamento = new ControladorDepartamento();
        inicializarComponentes();
        configurarVentana();
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
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Departamento"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ID
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField(30);
        panel.add(txtId, gbc);

        // Nombre
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField(30);
        panel.add(txtNombre, gbc);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        btnRegistrar = new JButton("Registrar");
        btnRegistrar.addActionListener(e -> registrarDepartamento());

        btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> actualizarDepartamento());

        btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(e -> eliminarDepartamento());

        btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscarDepartamento());

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(e -> limpiarCampos());

        btnListar = new JButton("Refrescar Lista");
        btnListar.addActionListener(e -> cargarDepartamentos());

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
        panel.setBorder(BorderFactory.createTitledBorder("Lista de Departamentos"));

        String[] columnas = {"ID", "Nombre"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaDepartamentos = new JTable(modeloTabla);
        tablaDepartamentos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaDepartamentos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 1) {
                    cargarDepartamentoSeleccionado();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaDepartamentos);
        scrollPane.setPreferredSize(new Dimension(600, 250));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void registrarDepartamento() {
        try {
            if (validarCampos()) {
                Departamento departamento = crearDepartamentoDesdeFormulario();
                controladorDepartamento.registrarDepartamento(
                        departamento.getId(),
                        departamento.getNombre(),
                        departamento.getDescripcion(),
                        departamento.getCorreoElectronico(),
                        departamento.getExtensionTel()
                );
                JOptionPane.showMessageDialog(this,
                        "Departamento registrado exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                cargarDepartamentos();
            }
        } catch (GlobalException | NoDataException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarDepartamento() {
        try {
            if (validarCampos()) {
                Departamento departamento = crearDepartamentoDesdeFormulario();
                controladorDepartamento.actualizarDepartamento(
                        departamento.getId(),
                        departamento.getNombre(),
                        departamento.getDescripcion(),
                        departamento.getCorreoElectronico(),
                        departamento.getExtensionTel()
                );
                JOptionPane.showMessageDialog(this,
                        "Departamento actualizado exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                cargarDepartamentos();
            }
        } catch (GlobalException | NoDataException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    private void eliminarDepartamento() {
        try {
            String id = txtId.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Debe ingresar un ID",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de eliminar este departamento?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                controladorDepartamento.eliminarDepartamento(id);
                JOptionPane.showMessageDialog(this,
                        "Departamento eliminado exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                cargarDepartamentos();
            }
        } catch (GlobalException | NoDataException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarDepartamento() {
        try {
            String id = txtId.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Debe ingresar un ID",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Departamento departamento = controladorDepartamento.buscarDepartamento(id);
            cargarDepartamentoEnFormulario(departamento);

        } catch (GlobalException | NoDataException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarDepartamentos() {
        try {
            modeloTabla.setRowCount(0);
            Collection<Departamento> departamentos = controladorDepartamento.listarDepartamentos();

            for (Departamento d : departamentos) {
                Object[] fila = {
                        d.getId(),
                        d.getNombre()
                };
                modeloTabla.addRow(fila);
            }
        } catch (GlobalException | NoDataException ex) {
            // Si no hay datos, simplemente no mostrar nada
        }
    }

    private void cargarDepartamentoSeleccionado() {
        int filaSeleccionada = tablaDepartamentos.getSelectedRow();
        if (filaSeleccionada != -1) {
            txtId.setText(modeloTabla.getValueAt(filaSeleccionada, 0).toString());
            txtNombre.setText(modeloTabla.getValueAt(filaSeleccionada, 1).toString());
        }
    }

    private void cargarDepartamentoEnFormulario(Departamento d) {
        txtId.setText(d.getId());
        txtNombre.setText(d.getNombre());
    }

    private Departamento crearDepartamentoDesdeFormulario() {
        return new Departamento(
                txtId.getText().trim(),
                txtNombre.getText().trim()
        );
    }

    private boolean validarCampos() {
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El ID es obligatorio",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
    }

    private void configurarVentana() {
        setTitle("Gestión de Departamentos");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InterfazDepartamento().setVisible(true);
        });
    }
}