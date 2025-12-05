package Presentacion;

import Controlss.ControlAnalisisBow;
import AccesoADatos.ServicioAnalisisBow;
import AccesoADatos.GlobalException;
import AccesoADatos.NoDataException;
import javax.swing.*;

public class ViewAnalisisBow  extends JFrame {
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JPanel root;
    private JTextField textField5;
    private JButton analizarButton;

    private final ControlAnalisisBow control = new ControlAnalisisBow();
    private final ServicioAnalisisBow servicioAnalisis = new ServicioAnalisisBow();

    public ViewAnalisisBow() {

        setContentPane(root);
        setTitle("AnálisisBow");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        pack();
        setLocationRelativeTo(null);
        setResizable(false);

        analizarButton.addActionListener(e -> onAnalizar());
    }

    private void onAnalizar() {

        String idTicket = textField1.getText().trim();
        String categoriaTecnica = textField2.getText().trim();
        String subcategoriaTecnica = textField5.getText().trim();

        if (idTicket.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debe ingresar el ID del Ticket.",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (categoriaTecnica.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debe escribir una categoría técnica.",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (subcategoriaTecnica.isEmpty()) {
            subcategoriaTecnica = "n/a";
        }

        String textoAnalizar = categoriaTecnica + " " + subcategoriaTecnica;

        String[] resultado = control.AnalizarDescripcion(textoAnalizar);


        String estado = (resultado != null && resultado.length > 0 &&
                resultado[0] != null && !resultado[0].trim().isEmpty())
                ? resultado[0]
                : "POSITIVO";

        String categoriaSugerida = (resultado != null && resultado.length > 1 &&
                resultado[1] != null && !resultado[1].trim().isEmpty())
                ? resultado[1]
                : "General";

        textField3.setText(estado);
        textField4.setText(categoriaSugerida);

        try {

            servicioAnalisis.insertarAnalisis(
                    idTicket,
                    estado,
                    categoriaSugerida
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Análisis guardado correctamente:\n\n" +
                            "Estado: " + estado + "\n" +
                            "Categoría sugerida: " + categoriaSugerida + "\n" +  // NUEVO
                            "Categoría técnica " + categoriaTecnica + "\n" +
                            "Subcategoría técnica" + subcategoriaTecnica,
                    "Guardado",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarCampos();

        } catch (GlobalException | NoDataException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al guardar en Oracle:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void limpiarCampos() {
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");
        textField5.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new ViewAnalisisBow().setVisible(true)
        );
    }
}