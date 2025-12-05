package Controlss;

import AccesoADatos.GlobalException;
import AccesoADatos.NoDataException;
import AccesoADatos.ServicioDiccionarioEmocional;
import diccionario.PalabraDiccionario;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ControlDiccionarioEmocional {

    ServicioDiccionarioEmocional sDE = new ServicioDiccionarioEmocional();

    public void Insertar(PalabraDiccionario p) {
        try {
            sDE.insertar(p);
        }
        catch (GlobalException ex)
        {
            Logger.getLogger(ControlDiccionarioEmocional.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (NoDataException ex)
        {
            Logger.getLogger(ControlDiccionarioEmocional.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public PalabraDiccionario Buscar(PalabraDiccionario p) {
        try {
            return sDE.buscar(p.getPalabra());
        } catch (GlobalException ex) {
            Logger.getLogger(ControlDiccionarioEmocional.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoDataException ex) {
            Logger.getLogger(ControlDiccionarioEmocional.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Collection<PalabraDiccionario> Listado() {
        try {
            return sDE.listarTodos();
        } catch (GlobalException ex) {
            Logger.getLogger(ControlDiccionarioEmocional.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoDataException ex) {
            Logger.getLogger(ControlDiccionarioEmocional.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void Eliminar(int id) {
        try {
            sDE.eliminar(id);
        } catch (GlobalException ex) {
            Logger.getLogger(ControlDiccionarioEmocional.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoDataException ex) {
            Logger.getLogger(ControlDiccionarioEmocional.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}