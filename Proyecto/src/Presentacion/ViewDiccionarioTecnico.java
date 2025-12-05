package Presentacion;

import Controlss.ControlDiccionarioTecnico;
import diccionario.PalabraDiccionario;
import javax.swing.*;
import java.util.Collection;

public class  ViewDiccionarioTecnico  extends JFrame{
    private JTextField textField1;
    private JTextField textField2;
    private JComboBox comboBox1;
    private JButton guardarPalabraButton;
    private JButton listarTodasButton;
    private JPanel root;

    private final ControlDiccionarioTecnico control = new ControlDiccionarioTecnico();

    public ViewDiccionarioTecnico() {
        setContentPane(root);
        setTitle("Diccionario Técnico");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        pack();
        setLocationRelativeTo(null);
        setResizable(false);

        guardarPalabraButton.addActionListener(e -> onGuardar());
        listarTodasButton.addActionListener(e -> onListar());
    }

    private void onGuardar() {
        String palabra = textField1.getText().trim();
        String categoria = (String) comboBox1.getSelectedItem();
        String descripcion = textField2.getText().trim(); // solo visual

        if (palabra.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar la palabra técnica.",
                    "Campo requerido",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        if (categoria == null || categoria.trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar una categoría.",
                    "Campo requerido",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        PalabraDiccionario p = new PalabraDiccionario(palabra, categoria);

        control.Insertar(p);

        JOptionPane.showMessageDialog(
                this,
                "Palabra técnica guardada:\n" +
                        palabra + " → " + categoria +
                        (descripcion.isEmpty() ? "" : "\nDescripción: " + descripcion),
                "Guardado correcto",
                JOptionPane.INFORMATION_MESSAGE
        );

        limpiarCampos();
    }

    private void onListar() {
        Collection<PalabraDiccionario> lista = control.Listado();

        if (lista == null || lista.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No hay palabras técnicas registradas.",
                    "Listado",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        StringBuilder sb = new StringBuilder("Palabras técnicas registradas:\n\n");
        for (PalabraDiccionario p : lista) {
            sb.append("- ").
                    append(p.getPalabra())
                    .append(" / ")
                    .append(p.getCategoria())
                    .append("\n");
        }

        JOptionPane.showMessageDialog(
                this,
                sb.toString(),
                "Listado de diccionario técnico",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void limpiarCampos() {
        textField1.setText("");
        textField2.setText("");
        if (comboBox1.getItemCount() > 0) {
            comboBox1.setSelectedIndex(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new ViewDiccionarioTecnico().setVisible(true)
        );
    }
}