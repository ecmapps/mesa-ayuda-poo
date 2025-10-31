import java.util.ArrayList;
import java.util.List;


public class GestionTicketsBow {

    private List<Usuario> usuarios;


    public GestionTicketsBow() {
        this.usuarios = new ArrayList<>();
    }

    public void registrarUsuario(Usuario usuario) {
        usuarios.add(usuario);
        System.out.println("Usuario registrado en el sistema: " + usuario.getNombre());
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
            System.out.println("Usuario no encontrado en el sistema");
        }
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }
}
