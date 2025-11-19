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

public class ServicioDiccionarioTecnico extends Servicio {

    private static final String insertar = "{ call agregarPalabraTecnica(?,?) }";
    private static final String buscar   = "{ ? = call buscarPalabraTecnica(?) }";
    private static final String listar   = "{ ? = call listarPalabrasTecnicas() }";
    private static final String eliminar = "{ call eliminarPalabraTecnica(?) }";

    public ServicioDiccionarioTecnico() { }

    public void insertar(PalabraDiccionario p) throws GlobalException, NoDataException {
        conectar();
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(insertar);
            pstmt.setString(1, p.getPalabra());
            pstmt.setString(2, p.getCategoria());

            boolean resultado = pstmt.execute();
            if (resultado) {
                throw new NoDataException("No se pudo insertar la palabra técnica.");
            }

        } catch (SQLException e) {
            throw new GlobalException("Error al insertar palabra técnica.");
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos.");
            }
        }
    }

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
                String texto    = rs.getString("palabraTecnica");
                String categoria = rs.getString("categoria");
                p = new PalabraDiccionario(texto, categoria);
            } else {
                throw new NoDataException("No existe esa palabra técnica.");
            }

        } catch (SQLException e) {
            throw new GlobalException("Error al buscar palabra técnica.");
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

    // LISTAR todas las palabras técnicas
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
                String texto    = rs.getString("palabraTecnica");
                String categoria = rs.getString("categoria");
                PalabraDiccionario p = new PalabraDiccionario(texto, categoria);
                lista.add(p);
            }

        } catch (SQLException e) {
            throw new GlobalException("Error al listar palabras técnicas.");
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
            throw new NoDataException("No hay datos en el diccionario técnico.");
        }

        return lista;
    }

    // ELIMINAR por id
    public void eliminar(int id) throws GlobalException, NoDataException {
        conectar();
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(eliminar);
            pstmt.setInt(1, id);

            int r = pstmt.executeUpdate();
            if (r == 0) {
                throw new NoDataException("No se eliminó la palabra técnica.");
            }

        } catch (SQLException e) {
            throw new GlobalException("Error al eliminar palabra técnica.");
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
        ServicioDiccionarioTecnico servicio = new ServicioDiccionarioTecnico();
        Collection<PalabraDiccionario> coleccion;

        try {
            //Insert
           // PalabraDiccionario nueva = new PalabraDiccionario("Tablet", "Moviles");
           // servicio.insertar(nueva);
           // System.out.println("Insertada palabra técnica.\n");

            //LISTAR
            //System.out.println("---- LISTA DE PALABRAS TÉCNICAS ----");
           //coleccion = servicio.listarTodos();
           //for (Iterator<PalabraDiccionario> it = coleccion.iterator(); it.hasNext();) {
              // PalabraDiccionario p = it.next();
              //  System.out.println(p.getPalabra() + " - " + p.getCategoria());
           // }

            //BUSCAR

          // System.out.println("\n---- BUSCAR 'error' ----");
           //PalabraDiccionario encontrada = servicio.buscar("error");
           // System.out.println("Resultado: " + encontrada.getPalabra()
                            // + " - " + encontrada.getCategoria());

            //ELIMINAR
           servicio.eliminar(1);
           System.out.println("Palabra técnica con id=1 eliminada.");


        } catch (Exception ex) {
            Logger.getLogger(ServicioDiccionarioTecnico.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
