package AccesoADatos;

import LogicaDeNegocio.Departamento;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.internal.OracleTypes;

public class ServicioDepartamento extends Servicio {

    private static final String registrarDepartamento = "{ call registrarDepartamento(?,?) }";
    private static final String buscarDepartamento = "{ ? = call buscarDepartamento(?) }";
    private static final String listarDepartamentos = "{ ? = call listarDepartamentos() }";
    private static final String actualizarDepartamento = "{ call actualizarDepartamento(?,?) }";
    private static final String eliminarDepartamento = "{ call eliminarDepartamento(?) }";

    public ServicioDepartamento() { }

    public Collection<Departamento> listarDepartamentos() throws GlobalException, NoDataException {
        conectar();
        ResultSet rs = null;
        ArrayList<Departamento> lista = new ArrayList<>();
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(listarDepartamentos);
            pstmt.registerOutParameter(1, OracleTypes.CURSOR);
            pstmt.execute();

            rs = (ResultSet) pstmt.getObject(1);

            while (rs.next()) {
                Departamento d = new Departamento(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("correoElectronico"),
                        rs.getString("extensionTel")
                );
                lista.add(d);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error al listar departamentos.");
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos.");
            }
        }

        if (lista.isEmpty()) throw new NoDataException("No hay departamentos registrados.");

        return lista;
    }

    public void registrarDepartamento(Departamento d) throws GlobalException, NoDataException {
        conectar();
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(registrarDepartamento);
            pstmt.setString(1, d.getId());
            pstmt.setString(2, d.getNombre());

            boolean resultado = pstmt.execute();

            if (resultado) {
                throw new NoDataException("No se pudo insertar el departamento.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error al registrar departamento. ¿ID duplicado?");
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos.");
            }
        }
    }

    public Departamento buscarDepartamento(String id) throws GlobalException, NoDataException {
        conectar();
        CallableStatement pstmt = null;
        ResultSet rs = null;
        Departamento d = null;

        try {
            pstmt = conexion.prepareCall(buscarDepartamento);
            pstmt.registerOutParameter(1, OracleTypes.CURSOR);
            pstmt.setString(2, id);
            pstmt.execute();

            rs = (ResultSet) pstmt.getObject(1);

            if (rs.next()) {
                d = new Departamento(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("correoElectronico"),
                        rs.getString("extensionTel")
                );
            } else {
                throw new NoDataException("Departamento no encontrado.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error al consultar departamento.");
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos.");
            }
        }

        return d;
    }

    public void actualizarDepartamento(Departamento d) throws GlobalException, NoDataException {
        conectar();
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(actualizarDepartamento);
            pstmt.setString(1, d.getId());
            pstmt.setString(2, d.getNombre());

            int resultado = pstmt.executeUpdate();

            if (resultado == 0) {
                throw new NoDataException("No se actualizó el departamento.");
            }

            System.out.println("Departamento actualizado correctamente.");

        } catch (SQLException e) {
            throw new GlobalException("Error al actualizar departamento: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos.");
            }
        }
    }

    public void eliminarDepartamento(String id) throws GlobalException, NoDataException {
        conectar();
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(eliminarDepartamento);
            pstmt.setString(1, id);

            int resultado = pstmt.executeUpdate();

            if (resultado == 0) {
                throw new NoDataException("No se eliminó el departamento.");
            }

            System.out.println("Departamento eliminado correctamente.");

        } catch (SQLException e) {
            throw new GlobalException("Error al eliminar departamento.");
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos.");
            }
        }
    }

    public static void main(String[] args) {
        ServicioDepartamento sd = new ServicioDepartamento();
        Collection coleccion;

        try {
            // Ejemplo de registrar
            // sd.registrarDepartamento(new Departamento("DEPT001", "Soporte Técnico"));

            // Listar departamentos
            System.out.println("Listado de Departamentos");
            coleccion = sd.listarDepartamentos();
            for (Iterator it = coleccion.iterator(); it.hasNext();) {
                Departamento d = (Departamento) it.next();
                System.out.println(d.toString());
            }

        } catch (Exception ex) {
            Logger.getLogger(ServicioDepartamento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}