package AccesoADatos;

import diccionario.RegistroEmocion;
import java.sql.CallableStatement;
import java.sql.SQLException;

public class ServicioRegistroEmociones extends Servicio {

    private static final String registrar =
            "{ call registrarEmocion(?,?,?,?,?) }";

    public ServicioRegistroEmociones() {
    }

    public void insertar(RegistroEmocion r)
            throws GlobalException, NoDataException {

        conectar();
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(registrar);
            pstmt.setString(1, r.getNombre());
            pstmt.setString(2, r.getEmocion());
            pstmt.setString(3, r.getIntensidad());
            pstmt.setString(4, r.getDuracion());
            pstmt.setString(5, r.getComentario());

            boolean resultado = pstmt.execute();
            if (resultado) {
                throw new NoDataException("No se realizó la inserción en RegistroEmociones.");
            }

        } catch (SQLException e) {
            throw new GlobalException("Error ejecutando registrarEmocion: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos en ServicioRegistroEmociones.");
            }
        }
    }
}

