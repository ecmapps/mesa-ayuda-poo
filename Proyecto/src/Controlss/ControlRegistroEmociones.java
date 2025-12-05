package Controlss;

import AccesoADatos.GlobalException;
import AccesoADatos.NoDataException;
import AccesoADatos.ServicioRegistroEmociones;
import diccionario.RegistroEmocion;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ControlRegistroEmociones {

    private final ServicioRegistroEmociones servicio = new ServicioRegistroEmociones();

    public void Insertar(RegistroEmocion r) {
        try {
            servicio.insertar(r);
        } catch (GlobalException | NoDataException ex) {
            Logger.getLogger(ControlRegistroEmociones.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
}