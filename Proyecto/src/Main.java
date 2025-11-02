
public class Main {
    public static void main(String[] args) {

        GestionTicketsBow gestor = new GestionTicketsBow();


        Usuario usuario1 = new Usuario(
                "123456789",
                "Juan",
                "Pérez",
                "García",
                "juan.perez@email.com",
                "password123",
                "88888888",
                Rol.ESTUDIANTE
        );

        Usuario usuario2 = new Usuario(
                "987654321",
                "María",
                "López",
                "Fernández",
                "maria.lopez@email.com",
                "password456",
                "77777777",
                Rol.ADMINISTRADOR
        );


        gestor.registrarUsuario(usuario1);
        gestor.registrarUsuario(usuario2);


        System.out.println("\n=== Usuarios creando tickets ===");
        usuario1.iniciarSesion();

        //Departamento de ejemplo
        Departamento departamento = new Departamento();
        departamento.setId("DEP001");
        departamento.setNombre("Soporte Técnico");
        
        Ticket ticket1 = new Ticket("TK001", "Problema con el sistema", "No puedo acceder a mi cuenta", Estado.NUEVO, usuario1,departamento);
        ticket1.crearTicket();

        Ticket ticket2 = new Ticket("TK002", "Error en la aplicación", "La aplicación se cierra inesperadamente", Estado.NUEVO, usuario1,departamento);
        ticket2.crearTicket();

        usuario2.iniciarSesion();
        Ticket ticket3 = new Ticket("TK003", "Consulta general", "¿Cómo cambio mi contraseña?", Estado.NUEVO, usuario2,departamento);
        ticket3.crearTicket();


        usuario1.verMisTickets();
        usuario2.verMisTickets();


        gestor.consultarTicketsUsuario("123456789");


        System.out.println("\n=== Actualizando estado de ticket ===");
        Ticket primerTicket = usuario1.getMisTickets().get(0);
        primerTicket.actualizarEstado(Estado.EN_PROGRESO);
        primerTicket.verDetalle();
    }
}