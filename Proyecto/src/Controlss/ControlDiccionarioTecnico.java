package Controlss;

import AccesoADatos.GlobalException;
import AccesoADatos.NoDataException;
import AccesoADatos.ServicioDiccionarioTecnico;
import diccionario.PalabraDiccionario;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ControlDiccionarioTecnico {

    ServicioDiccionarioTecnico sDT = new ServicioDiccionarioTecnico();

    public void Insertar(PalabraDiccionario p) {
        try {
            sDT.insertar(p);
        } catch (GlobalException ex) {
            Logger.getLogger(ControlDiccionarioTecnico.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoDataException ex) {
            Logger.getLogger(ControlDiccionarioTecnico.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public PalabraDiccionario Buscar(PalabraDiccionario p) {
        try {
            return sDT.buscar(p.getPalabra());
        } catch (GlobalException ex) {
            Logger.getLogger(ControlDiccionarioTecnico.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoDataException ex) {
            Logger.getLogger(ControlDiccionarioTecnico.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Collection<PalabraDiccionario> Listado() {
        try {
            return sDT.listarTodos();
        } catch (GlobalException ex) {
            Logger.getLogger(ControlDiccionarioTecnico.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoDataException ex) {
            Logger.getLogger(ControlDiccionarioTecnico.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void Eliminar(int id) {
        try {
            sDT.eliminar(id);
        } catch (GlobalException ex) {
            Logger.getLogger(ControlDiccionarioTecnico.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoDataException ex) {
            Logger.getLogger(ControlDiccionarioTecnico.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}