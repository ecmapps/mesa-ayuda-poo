package AccesoADatos;

import diccionario.PalabraDiccionario;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.internal.OracleTypes;

public class ServicioDiccionarioEmocional extends Servicio {

    private static final String insertar = "{ call agregarPalabraEmocional(?,?) }";
    private static final String buscar   = "{ ? = call buscarPalabraEmocional(?) }";
    private static final String listar   = "{ ? = call listarPalabrasEmocionales() }";
    private static final String eliminar = "{ call eliminarPalabraEmocional(?) }";

    public ServicioDiccionarioEmocional() { }

    public void insertar(PalabraDiccionario p) throws GlobalException, NoDataException {
        conectar();
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(insertar);
            pstmt.setString(1, p.getPalabra());
            pstmt.setString(2, p.getCategoria());

            boolean resultado = pstmt.execute();
            if (resultado) {
                throw new NoDataException("No se pudo insertar la palabra emocional.");
            }

        } catch (SQLException e) {
            throw new GlobalException("Error al insertar palabra emocional.");
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos.");
            }
        }
    }

    // buscar palabra emocional por texto
    public PalabraDiccionario buscar(String palabra) throws GlobalException, NoDataException {
        conectar();

        ResultSet rs = null;
        PalabraDiccionario p = null;
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(buscar);
            pstmt.registerOutParameter(1, OracleTypes.CURSOR);
            pstmt.setString(2, palabra);
            pstmt.execute();

            rs = (ResultSet) pstmt.getObject(1);

            if (rs.next()) {
                String texto   = rs.getString("palabraEmocional");
                String emocion = rs.getString("emocion");
                p = new PalabraDiccionario(texto, emocion);
            } else {
                throw new NoDataException("No existe esa palabra emocional.");
            }

        } catch (SQLException e) {
            throw new GlobalException("Error al buscar palabra emocional.");
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos.");
            }
        }

        return p;
    }

    // LISTAR todas las palabras emocionales
    public Collection<PalabraDiccionario> listarTodos() throws GlobalException, NoDataException {
        conectar();

        ResultSet rs = null;
        ArrayList<PalabraDiccionario> lista = new ArrayList<PalabraDiccionario>();
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(listar);
            pstmt.registerOutParameter(1, OracleTypes.CURSOR);
            pstmt.execute();

            rs = (ResultSet) pstmt.getObject(1);

            while (rs.next()) {
                String texto   = rs.getString("palabraEmocional");
                String emocion = rs.getString("emocion");
                PalabraDiccionario p = new PalabraDiccionario(texto, emocion);
                lista.add(p);
            }

        } catch (SQLException e) {
            throw new GlobalException("Error al listar palabras emocionales.");
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos.");
            }
        }

        if (lista.isEmpty()) {
            throw new NoDataException("No hay datos en el diccionario emocional.");
        }

        return lista;
    }

    // eliminar por id
    public void eliminar(int id) throws GlobalException, NoDataException {
        conectar();
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(eliminar);
            pstmt.setInt(1, id);

            int r = pstmt.executeUpdate();
            if (r == 0) {
                throw new NoDataException("No se eliminó la palabra emocional.");
            }

        } catch (SQLException e) {
            throw new GlobalException("Error al eliminar palabra emocional.");
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos.");
            }
        }
    }

    //pruebas
    public static void main(String[] args) {
        ServicioDiccionarioEmocional servicio = new ServicioDiccionarioEmocional();
        Collection<PalabraDiccionario> coleccion;

        try {
            //INSERTS deprueba
           //PalabraDiccionario nueva = new PalabraDiccionario("superfeliz", "positivo");
            //servicio.insertar(nueva);
           //System.out.println("Insertada palabra emocional.\n");


            // LISTAR
            System.out.println("LISTA DE PALABRAS EMOCIONALES");
           coleccion = servicio.listarTodos();
            for (Iterator<PalabraDiccionario> it = coleccion.iterator(); it.hasNext();) {
               PalabraDiccionario p = it.next();
                System.out.println(p.getPalabra() + " - " + p.getCategoria());
            }

            //BUSCAR
           // System.out.println("\n---- BUSCAR 'frustrado' ----");
           // PalabraDiccionario encontrada = servicio.buscar("frustrado");
            //System.out.println("Resultado: " + encontrada.getPalabra()
                             //  + " - " + encontrada.getCategoria());

            // ELIMINAR
           //servicio.eliminar(22);
           //System.out.println("Palabra emocional con id=22 eliminada.");

        } catch (Exception ex) {
            Logger.getLogger(ServicioDiccionarioEmocional.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
