package AccesoADatos;

import java.sql.CallableStatement;
import java.sql.SQLException;

public class ServicioAnalisisBow extends Servicio {

    private static final String registrar = "{ call registrarAnalisis(?,?,?) }";

    public ServicioAnalisisBow() {
    }

    public void insertarAnalisis(String ticketId,
                                 String estadoAnimo,
                                 String categoriaSugerida)
            throws GlobalException, NoDataException {

        conectar();
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(registrar);
            pstmt.setString(1, ticketId);
            pstmt.setString(2, estadoAnimo);
            pstmt.setString(3, categoriaSugerida);

            boolean resultado = pstmt.execute();
            if (resultado) {
                throw new NoDataException("No se realizó la inserción del análisis.");
            }

        } catch (SQLException e) {
            throw new GlobalException("Error ejecutando registrarAnalisis: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos en ServicioAnalisisBow.");
            }
        }
    }
}
