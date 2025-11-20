package AccesoADatos;

import LogicaDeNegocio.Departamento;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

// ServicioDepartamento: Clase que gestiona las operaciones CRUD de departamentos en la base de datos.
public class ServicioDepartamento extends Servicio {

    private static final String insertar = "{ call insertarDepartamento(?, ?, ?, ?, ?) }";
    private static final String actualizar = "{ call actualizarDepartamento(?, ?, ?, ?, ?) }";
    private static final String eliminar = "{ call eliminarDepartamento(?) }";
    private static final String listar = "{ ? = call listarDepartamentos() }";

    public void insertar(Departamento departamento) throws GlobalException, NoDataException {
        conectar();
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(insertar);
            pstmt.setString(1, departamento.getId());
            pstmt.setString(2, departamento.getNombre());
            pstmt.setString(3, departamento.getDescripcion());
            pstmt.setString(4, departamento.getCorreoElectronico());
            pstmt.setString(5, departamento.getExtensionTel());

            boolean resultado = pstmt.execute();
            if (resultado) {
                throw new NoDataException("No se pudo insertar el departamento.");
            }
        } catch (SQLException e) {
            throw new GlobalException("Error al insertar el departamento.");
        } finally {
            cerrarRecursos(pstmt);
        }
    }

    public void actualizar(Departamento departamento) throws GlobalException, NoDataException {
        conectar();
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(actualizar);
            pstmt.setString(1, departamento.getId());
            pstmt.setString(2, departamento.getNombre());
            pstmt.setString(3, departamento.getDescripcion());
            pstmt.setString(4, departamento.getCorreoElectronico());
            pstmt.setString(5, departamento.getExtensionTel());

            boolean resultado = pstmt.execute();
            if (resultado) {
                throw new NoDataException("No se pudo actualizar el departamento.");
            }
        } catch (SQLException e) {
            throw new GlobalException("Error al actualizar el departamento.");
        } finally {
            cerrarRecursos(pstmt);
        }
    }

    public void eliminar(String id) throws GlobalException, NoDataException {
        conectar();
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(eliminar);
            pstmt.setString(1, id);

            boolean resultado = pstmt.execute();
            if (resultado) {
                throw new NoDataException("No se pudo eliminar el departamento.");
            }
        } catch (SQLException e) {
            throw new GlobalException("Error al eliminar el departamento.");
        } finally {
            cerrarRecursos(pstmt);
        }
    }

    public Collection<Departamento> listarTodos() throws GlobalException, NoDataException {
        conectar();
        CallableStatement pstmt = null;
        ResultSet rs = null;
        Collection<Departamento> departamentos = new ArrayList<>();

        try {
            pstmt = conexion.prepareCall(listar);
            pstmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            pstmt.execute();

            rs = (ResultSet) pstmt.getObject(1);
            while (rs.next()) {
                Departamento departamento = new Departamento(
                    rs.getString("id"),
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getString("correoElectronico"),
                    rs.getString("extensionTel")
                );
                departamentos.add(departamento);
            }
        } catch (SQLException e) {
            throw new GlobalException("Error al listar los departamentos.");
        } finally {
            cerrarRecursos(rs, pstmt);
        }

        return departamentos;
    }
}