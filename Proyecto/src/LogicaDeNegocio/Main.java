package LogicaDeNegocio;

import diccionario.AnalisisBow;

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

        //LogicaDeNegocio.Departamento de ejemplo
        Departamento depTecnico = new Departamento();
        depTecnico.setId("DEP001");
        depTecnico.setNombre("Soporte Técnico");

        Departamento depOrientacion = new Departamento();
        depOrientacion.setId("DEP002");
        depOrientacion.setNombre("Orientación Estudiantil");
        
        Ticket ticket1 = new Ticket("TK001", "Problema con el sistema", "No puedo acceder a mi cuenta", Estado.NUEVO, usuario1,depTecnico);
        ticket1.crearTicket();

        Ticket ticket2 = new Ticket("TK002", "Error en la aplicación", "La aplicación se cierra inesperadamente", Estado.NUEVO, usuario1,depOrientacion);
        ticket2.crearTicket();

        usuario2.iniciarSesion();
        Ticket ticket3 = new Ticket("TK003", "Consulta general", "¿Cómo cambio mi contraseña?", Estado.NUEVO, usuario2,depTecnico);
        ticket3.crearTicket();


        usuario1.verMisTickets();
        usuario2.verMisTickets();


        gestor.consultarTicketsUsuario("123456789");
        //Revisar por departamento
        gestor.ConsultarTicketsDepartamento("DEP001");
        gestor.ConsultarTicketsDepartamento("DEP002");

        //diccionario.AnalisisBow
        AnalisisBow analisis = new AnalisisBow();
        analisis.analizarPalabras(ticket3);

        System.out.println("\n=== Actualizando estado de ticket ===");
        Ticket primerTicket = usuario1.getMisTickets().get(0);
        primerTicket.actualizarEstado(Estado.EN_PROGRESO);
        primerTicket.verDetalle();
    }
}