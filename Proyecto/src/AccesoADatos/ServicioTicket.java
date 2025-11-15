package AccesoADatos;

import LogicaDeNegocio.Ticket;
import LogicaDeNegocio.Estado;
import LogicaDeNegocio.Usuario;
import LogicaDeNegocio.Departamento;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.internal.OracleTypes;

public class ServicioTicket extends Servicio {

    private static final String crearTicket = "{ call crearTicket(?,?,?,?,?,?) }";
    private static final String buscarTicket = "{ ? = call buscarTicket(?) }";
    private static final String listarTickets = "{ ? = call listarTickets() }";
    private static final String consultarTicketsUsuario = "{ ? = call consultarTicketsUsuario(?) }";
    private static final String consultarTicketsDepartamento = "{ ? = call consultarTicketsDepartamento(?) }";
    private static final String actualizarEstado = "{ call actualizarEstado(?,?) }";

    public ServicioTicket() { }


    public Collection<Ticket> listarTickets() throws GlobalException, NoDataException {
        conectar();
        ResultSet rs = null;
        ArrayList<Ticket> lista = new ArrayList<>();
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(listarTickets);
            pstmt.registerOutParameter(1, OracleTypes.CURSOR);
            pstmt.execute();

            rs = (ResultSet) pstmt.getObject(1);

            while (rs.next()) {
                // Crear objetos Usuario y Departamento con solo el ID
                // En una implementación completa, podrías hacer consultas adicionales
                Usuario usuario = new Usuario();
                usuario.setCedula(rs.getString("usuarioCedula"));

                Departamento departamento = new Departamento();
                departamento.setId(rs.getString("departamentoId"));

                Ticket t = new Ticket(
                        rs.getString("id"),
                        rs.getString("asunto"),
                        rs.getString("descripcion"),
                        Estado.valueOf(rs.getString("estado")),
                        usuario,
                        departamento
                );
                lista.add(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error al listar tickets.");
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos.");
            }
        }

        if (lista.isEmpty()) throw new NoDataException("No hay tickets registrados.");

        return lista;
    }


    public void crearTicket(Ticket t) throws GlobalException, NoDataException {
        conectar();
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(crearTicket);
            pstmt.setString(1, t.getId());
            pstmt.setString(2, t.getAsunto());
            pstmt.setString(3, t.getDescripcion());
            pstmt.setString(4, t.getEstado().toString());
            pstmt.setString(5, t.getUsuario().getCedula());
            pstmt.setString(6, t.getDepartamento().getId());

            boolean resultado = pstmt.execute();

            if (resultado) {
                throw new NoDataException("No se pudo crear el ticket.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error al crear ticket. ¿ID duplicado?");
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos.");
            }
        }
    }


    public Ticket buscarTicket(String id) throws GlobalException, NoDataException {
        conectar();
        CallableStatement pstmt = null;
        ResultSet rs = null;
        Ticket t = null;

        try {
            pstmt = conexion.prepareCall(buscarTicket);
            pstmt.registerOutParameter(1, OracleTypes.CURSOR);
            pstmt.setString(2, id);
            pstmt.execute();

            rs = (ResultSet) pstmt.getObject(1);

            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setCedula(rs.getString("usuarioCedula"));

                Departamento departamento = new Departamento();
                departamento.setId(rs.getString("departamentoId"));

                t = new Ticket(
                        rs.getString("id"),
                        rs.getString("asunto"),
                        rs.getString("descripcion"),
                        Estado.valueOf(rs.getString("estado")),
                        usuario,
                        departamento
                );
            } else {
                throw new NoDataException("Ticket no encontrado.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error al buscar ticket.");
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos.");
            }
        }

        return t;
    }


    public Collection<Ticket> consultarTicketsUsuario(String cedula) throws GlobalException, NoDataException {
        conectar();
        ResultSet rs = null;
        ArrayList<Ticket> lista = new ArrayList<>();
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(consultarTicketsUsuario);
            pstmt.registerOutParameter(1, OracleTypes.CURSOR);
            pstmt.setString(2, cedula);
            pstmt.execute();

            rs = (ResultSet) pstmt.getObject(1);

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setCedula(rs.getString("usuarioCedula"));

                Departamento departamento = new Departamento();
                departamento.setId(rs.getString("departamentoId"));

                Ticket t = new Ticket(
                        rs.getString("id"),
                        rs.getString("asunto"),
                        rs.getString("descripcion"),
                        Estado.valueOf(rs.getString("estado")),
                        usuario,
                        departamento
                );
                lista.add(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error al consultar tickets por usuario.");
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos.");
            }
        }

        if (lista.isEmpty()) throw new NoDataException("No hay tickets para este usuario.");

        return lista;
    }


    public Collection<Ticket> consultarTicketsDepartamento(String departamentoId) throws GlobalException, NoDataException {
        conectar();
        ResultSet rs = null;
        ArrayList<Ticket> lista = new ArrayList<>();
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(consultarTicketsDepartamento);
            pstmt.registerOutParameter(1, OracleTypes.CURSOR);
            pstmt.setString(2, departamentoId);
            pstmt.execute();

            rs = (ResultSet) pstmt.getObject(1);

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setCedula(rs.getString("usuarioCedula"));

                Departamento departamento = new Departamento();
                departamento.setId(rs.getString("departamentoId"));

                Ticket t = new Ticket(
                        rs.getString("id"),
                        rs.getString("asunto"),
                        rs.getString("descripcion"),
                        Estado.valueOf(rs.getString("estado")),
                        usuario,
                        departamento
                );
                lista.add(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error al consultar tickets por departamento.");
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos.");
            }
        }

        if (lista.isEmpty()) throw new NoDataException("No hay tickets para este departamento.");

        return lista;
    }


    public void actualizarEstado(String id, Estado nuevoEstado) throws GlobalException, NoDataException {
        conectar();
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(actualizarEstado);
            pstmt.setString(1, id);
            pstmt.setString(2, nuevoEstado.toString());

            int resultado = pstmt.executeUpdate();

            if (resultado == 0) {
                throw new NoDataException("No se actualizó el estado del ticket.");
            }

            System.out.println("Estado del ticket actualizado correctamente.");

        } catch (SQLException e) {
            throw new GlobalException("Error al actualizar estado del ticket: " + e.getMessage());
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
        ServicioTicket st = new ServicioTicket();
        Collection coleccion;

        try {
//            // Ejemplo de crear ticket
//            Usuario usuario1 = new Usuario();
//            usuario1.setCedula("101110111");
//
//            Departamento depto = new Departamento();
//            depto.setId("DEPT001");
//
//            st.crearTicket(new Ticket("TKT001", "Problema con impresora",
//                "La impresora no enciende", Estado.NUEVO , usuario1, depto));

//             Ejemplo de buscar ticket
//            System.out.println("Consulta del Ticket...");
//            Ticket ticket = st.buscarTicket("TKT001");
//            System.out.println(ticket.toString());

            // Ejemplo de actualizar estado
//            st.actualizarEstado("TKT001", Estado.EN_PROGRESO);
//
//            // Listar todos los tickets
//            System.out.println("Listado de Tickets");
//            coleccion = st.listarTickets();
//            for (Iterator it = coleccion.iterator(); it.hasNext();) {
//                Ticket l = (Ticket) it.next();
//                System.out.println(l.toString());
//            }

            // Listar tickets por usuario
            System.out.println("\nTickets del Usuario 101110111");
            coleccion = st.consultarTicketsUsuario("101110111");
            for (Iterator it = coleccion.iterator(); it.hasNext();) {
                Ticket l = (Ticket) it.next();
                System.out.println(l.toString());
            }

            // Listar tickets por departamento
//            System.out.println("\nTickets del Departamento DEPT001");
//            coleccion = st.consultarTicketsDepartamento("DEPT001");
//            for (Iterator it = coleccion.iterator(); it.hasNext();) {
//                Ticket l = (Ticket) it.next();
//                System.out.println(l.toString());
//            }

        } catch (Exception ex) {
            Logger.getLogger(ServicioTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}