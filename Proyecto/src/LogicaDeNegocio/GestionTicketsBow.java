package LogicaDeNegocio;

import java.util.ArrayList;
import java.util.List;


public class GestionTicketsBow {

    private List<Usuario> usuarios;


    public GestionTicketsBow() {
        this.usuarios = new ArrayList<>();
    }

    public void registrarUsuario(Usuario usuario) {
        usuarios.add(usuario);
        System.out.println("LogicaDeNegocio.Usuario registrado en el sistema: " + usuario.getNombre());
    }
    public void consultarTicketsUsuario(String cedula) {
        System.out.println("\n=== Consultando tickets del usuario con cédula: " + cedula + " ===");
        boolean encontrado = false;
        for (Usuario usuario : usuarios) {
            if (usuario.getCedula().equals(cedula)) {
                encontrado = true;
                if (usuario.getMisTickets().isEmpty()) {
                    System.out.println("El usuario no tiene tickets creados");
                } else {
                    for (Ticket ticket : usuario.getMisTickets()) {
                        ticket.verDetalle();
                    }
                }
                break;
            }
        }
        if (!encontrado) {
            System.out.println("LogicaDeNegocio.Usuario no encontrado en el sistema");
        }
    }
    public void ConsultarTicketsDepartamento(String idDepartamento) {
        System.out.println("\n=== Consultando tickets del departamento con ID: " + idDepartamento + " ===");
        boolean encontrado = false;
        for (Usuario usuario : usuarios) {
            for (Ticket ticket : usuario.getMisTickets()) {
                if (ticket.getDepartamento().getId().equals(idDepartamento)) {
                    encontrado = true;
                    ticket.verDetalle();
                }
            }
        }
        if (!encontrado) {
            System.out.println("No se encontraron tickets para el departamento especificado");
        }
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }
}
