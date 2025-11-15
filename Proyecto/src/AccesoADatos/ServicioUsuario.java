package AccesoADatos;

import LogicaDeNegocio.Usuario;
import LogicaDeNegocio.Rol;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.internal.OracleTypes;

public class ServicioUsuario extends Servicio {

    private static final String registrarUsuario = "{ call registrarUsuario(?,?,?,?,?,?,?,?) }";
    private static final String buscarUsuario = "{ ? = call buscarUsuario(?) }";
    private static final String listarUsuarios = "{ ? = call listarUsuarios() }";
    private static final String actualizarUsuario = "{ call actualizarUsuario(?,?,?,?,?,?,?,?) }";
    private static final String eliminarUsuario = "{ call eliminarUsuario(?) }";

    public ServicioUsuario() { }


    public Collection<Usuario> listarUsuarios() throws GlobalException, NoDataException {
        conectar();
        ResultSet rs = null;
        ArrayList<Usuario> lista = new ArrayList<>();
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(listarUsuarios);
            pstmt.registerOutParameter(1, OracleTypes.CURSOR);
            pstmt.execute();

            rs = (ResultSet) pstmt.getObject(1);

            while (rs.next()) {
                Usuario u = new Usuario(
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("apellido1"),
                        rs.getString("apellido2"),
                        rs.getString("correoElectronico"),
                        rs.getString("contrasenna"),
                        rs.getString("numTelefono"),
                        Rol.valueOf(rs.getString("rol"))
                );
                lista.add(u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error al listar usuarios.");
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos.");
            }
        }

        if (lista.isEmpty()) throw new NoDataException("No hay usuarios registrados.");

        return lista;
    }



    public void registrarUsuario(Usuario u) throws GlobalException, NoDataException {
        conectar();
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall( registrarUsuario );
            pstmt.setString(1, u.getCedula());
            pstmt.setString(2, u.getNombre());
            pstmt.setString(3, u.getApellido1());
            pstmt.setString(4, u.getApellido2());
            pstmt.setString(5, u.getCorreoElectronico());
            pstmt.setString(6, u.getContrasenna());
            pstmt.setString(7, u.getNumTelefono());
            pstmt.setString(8, u.getRol().toString());

            boolean resultado = pstmt.execute();

            if (resultado) {
                throw new NoDataException("No se pudo insertar el usuario.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error al registrar usuario. ¿Cédula duplicada?");
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos.");
            }
        }
    }



    public Usuario buscarUsuario(String cedula) throws GlobalException, NoDataException {

        conectar();
        CallableStatement pstmt = null;
        ResultSet rs = null;
        Usuario u = null;

        try {
            pstmt = conexion.prepareCall(buscarUsuario);
            pstmt.registerOutParameter(1, OracleTypes.CURSOR);
            pstmt.setString(2, cedula);
            pstmt.execute();

            rs = (ResultSet) pstmt.getObject(1);

            if (rs.next()) {
                u = new Usuario(
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("apellido1"),
                        rs.getString("apellido2"),
                        rs.getString("correoElectronico"),
                        rs.getString("contrasenna"),
                        rs.getString("numTelefono"),
                        Rol.valueOf(rs.getString("rol"))
                );
            } else {
                throw new NoDataException("Usuario no encontrado.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error al consultar usuario.");
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos.");
            }
        }

        return u;
    }



    public void actualizarUsuario(Usuario u) throws GlobalException, NoDataException {
        conectar();
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(actualizarUsuario);

            pstmt.setString(1, u.getCedula());
            pstmt.setString(2, u.getNombre());
            pstmt.setString(3, u.getApellido1());
            pstmt.setString(4, u.getApellido2());
            pstmt.setString(5, u.getCorreoElectronico());
            pstmt.setString(6, u.getContrasenna());
            pstmt.setString(7, u.getNumTelefono());
            pstmt.setString(8, u.getRol().toString());

            int resultado = pstmt.executeUpdate();

            if (resultado == 0) {
                throw new NoDataException("No se actualizó el usuario.");
            }

            System.out.println("Usuario actualizado correctamente.");

        } catch (SQLException e) {
            throw new GlobalException("Error al actualizar usuario: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos.");
            }
        }
    }




    public void eliminarUsuario(String cedula) throws GlobalException, NoDataException {
        conectar();
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(eliminarUsuario);
            pstmt.setString(1, cedula);

            int resultado = pstmt.executeUpdate();

            if (resultado == 0) {
                throw new NoDataException("No se eliminó el usuario.");
            }

            System.out.println("Usuario eliminado correctamente.");

        } catch (SQLException e) {
            throw new GlobalException("Error al eliminar usuario.");
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

        ServicioUsuario su = new ServicioUsuario();
        Collection coleccion;

       try {
//            su.registrarUsuario(new Usuario("118580641","Bryan","Millon", "Ojeda", "Bryanmillon@gmail.com", "Password", "83637692", Rol.ESTUDIANTE));
//            coleccion =  su.listarUsuarios();
//           for (Iterator it = coleccion.iterator(); it.hasNext();) {
//               Usuario l = (Usuario) it.next();
//               System.out.println(""+l.toString());}

////           // Ejemplo de actualizar y su listado
//           su.actualizarUsuario(new Usuario("118580641","Steven","Millon", "Ojeda", "stevenmillon@gmail.com", "passwp", "83637692", Rol.ADMINISTRADOR));
//           coleccion =  su.listarUsuarios();
//           for (Iterator it = coleccion.iterator(); it.hasNext();) {
//               Usuario l = (Usuario) it.next();
//               System.out.println(""+l.toString());
//          }
//            System.out.print("Consulta del Usuario...");
//            // Ejemplo de consultar y su listado
//            Usuario Usua =  su.buscarUsuario("118580641");
//            System.out.println(""+Usua.toString());
//
           // Ejemplo de eliminar y su listado
//           su.eliminarUsuario("118580641");
//           coleccion =  su.listarUsuarios();
//           for (Iterator it = coleccion.iterator(); it.hasNext();) {
//               Usuario l = (Usuario) it.next();
//               System.out.println(""+l.toString());
//           }


            //listar Usuarios

            System.out.println("Listado de Usuarios");
            coleccion = su.listarUsuarios();
            for (Iterator it = coleccion.iterator(); it.hasNext();) {
                Usuario l = (Usuario) it.next();
                System.out.println(l.toString());
            }

        } catch (Exception ex) {
            Logger.getLogger(ServicioUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
