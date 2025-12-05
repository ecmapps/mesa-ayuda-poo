package Controlss;

import AccesoADatos.GlobalException;
import AccesoADatos.NoDataException;
import AccesoADatos.ServicioAnalisisBow;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ControlAnalisisBow {

    private final ServicioAnalisisBow servicioAnalisis = new ServicioAnalisisBow();

    public ControlAnalisisBow() {
    }
    public String[] AnalizarYGuardar(String idTicket, String descripcion) {
        String[] resultado = AnalizarDescripcion(descripcion);

        if (resultado == null) {
            return null;
        }

        String estado = resultado[0];
        String categoria = resultado[1];

        try {
            servicioAnalisis.insertarAnalisis(idTicket, estado, categoria);
        } catch (GlobalException | NoDataException ex) {
            Logger.getLogger(ControlAnalisisBow.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return resultado;
    }

    public String[] AnalizarDescripcion(String descripcion) {

        if (descripcion == null || descripcion.trim().isEmpty()) {
            return new String[]{"Neutro", "General"};
        }

        String estado = detectarEstadoEmocionalPorTexto(descripcion);
        String categoriaTecnica = detectarCategoriaTecnicaPorTexto(descripcion);

        return new String[]{estado, categoriaTecnica};
    }
    private String detectarEstadoEmocionalPorTexto(String descripcion) {

        String texto = descripcion.toLowerCase();
        if (texto.contains("feliz") ||
                texto.contains("contento") ||
                texto.contains("alegre")) {
            return "Feliz";
        }
        if (texto.contains("triste") ||
                texto.contains("deprimido") ||
                texto.contains("decaido") ||
                texto.contains("decaído")) {
            return "Triste";
        }
        if (texto.contains("enojado") ||
                texto.contains("molesto") ||
                texto.contains("furioso") ||
                texto.contains("frustrado")) {
            return "Enojado";
        }

        return "Neutro";
    }
    private String detectarCategoriaTecnicaPorTexto(String descripcion) {

        String texto = descripcion.toLowerCase().trim();

        if (texto.contains("pantalla") ||
                texto.contains("impresora")) {
            return "HARDWARE";
        }
        if (texto.contains("conexion") ||
                texto.contains("conexión") ||
                texto.contains("wifi") ||
                texto.contains("red") ||
                texto.contains("redes")) {
            return "RED";
        }
        if (texto.contains("correo") ||
                texto.contains("login")) {
            return "SOFTWARE";
        }
        if (texto.contains("contraseña") ||
                texto.contains("contrasena") ||
                texto.contains("virus")) {
            return "SEGURIDAD";
        }
        if (texto.contains("tablet") ||
                texto.contains("movil") ||
                texto.contains("móvil") ||
                texto.contains("celular")) {
            return "Moviles";
        }
        return "General";
    }
}