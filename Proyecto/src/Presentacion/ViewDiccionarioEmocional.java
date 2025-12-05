package Presentacion;

import Controlss.ControlDiccionarioEmocional;
import Controlss.ControlRegistroEmociones;
import diccionario.RegistroEmocion;
import javax.swing.*;

public class ViewDiccionarioEmocional extends JFrame {
    private JPanel roout;
    private JComboBox comboBox1;
    private JTextField textField1;
    private JTextField textField2;
    private JButton enviarFormularioButton;
    private JComboBox comboBox2;
    private JComboBox comboBox3;

    private final ControlDiccionarioEmocional controlDic = new ControlDiccionarioEmocional();
    private final ControlRegistroEmociones controlReg = new ControlRegistroEmociones();//fin

    public ViewDiccionarioEmocional() {
        setContentPane(roout);
        setTitle("Formulario De Emociones");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        enviarFormularioButton.addActionListener(e -> onEnviar());
    }

    private void onEnviar() {
        String nombre  = textField2.getText().trim();
        String emocion  = (String) comboBox1.getSelectedItem();
        String intensidad = (String) comboBox2.getSelectedItem();
        String duracion  = (String) comboBox3.getSelectedItem();
        String comentario =textField1.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Por favor escriba su nombre.",
                    "Campo obligatorio",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (emocion == null)   emocion = "";
        if (intensidad == null) intensidad = "";
        if (duracion == null)  duracion = "";

        String mensaje =
                "Hola " + nombre + ",\n" +
                        "tu emoción es: " + emocion + ".\n" +
                        "La intensidad de la emoción fue: " + intensidad + ".\n" +
                        "La duración de la emoción fue: " + duracion + ".\n" +
                        "Tu comentario fue: " + (comentario.isEmpty() ? "sin comentario" : comentario);

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Emoción registrada",
                JOptionPane.INFORMATION_MESSAGE
        );
        RegistroEmocion r = new RegistroEmocion(
                nombre,
                emocion,
                intensidad,
                duracion,
                comentario
        );
        controlReg.Insertar(r);

        System.out.println("Registro guardado en RegistroEmociones para: " + nombre);
        limpiarFormulario();
    }

    private void limpiarFormulario() {
        textField2.setText("");
        comboBox1.setSelectedIndex(0);
        comboBox2.setSelectedIndex(0);
        comboBox3.setSelectedIndex(0);
        textField1.setText("");
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new ViewDiccionarioEmocional().setVisible(true)
        );
    }
}