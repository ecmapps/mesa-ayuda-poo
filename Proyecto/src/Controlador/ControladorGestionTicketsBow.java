package Controlador;

import AccesoADatos.*;
import LogicaDeNegocio.*;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ControladorGestionTicketsBow {

    private ControladorTicket controladorTicket;
    private ControladorUsuario controladorUsuario;
    private ControladorDepartamento controladorDepartamento;

    public ControladorGestionTicketsBow() {
        this.controladorTicket = new ControladorTicket();
        this.controladorUsuario = new ControladorUsuario();
        this.controladorDepartamento = new ControladorDepartamento();
    }


    public ReporteSistema generarReporteCompleto() throws GlobalException, NoDataException {
        ReporteSistema reporte = new ReporteSistema();

        try {
            // Obtener estadísticas de tickets
            ControladorTicket.EstadisticasTicket stats = controladorTicket.obtenerEstadisticas();
            reporte.totalTickets = stats.getTotal();
            reporte.ticketsNuevos = stats.getNuevos();
            reporte.ticketsEnProgreso = stats.getEnProgreso();
            reporte.ticketsResueltos = stats.getResueltos();
            reporte.ticketsCerrados = stats.getCerrados();

            // Obtener total de usuarios por rol
            reporte.totalEstudiantes = controladorUsuario.contarUsuariosPorRol(Rol.ESTUDIANTE);
            reporte.totalFuncionarios = controladorUsuario.contarUsuariosPorRol(Rol.FUNCIONARIO);
            reporte.totalAdministradores = controladorUsuario.contarUsuariosPorRol(Rol.ADMINISTRADOR);

            // Obtener total de departamentos
            reporte.totalDepartamentos = controladorDepartamento.contarDepartamentos();

        } catch (NoDataException e) {
            // Si no hay datos, los valores quedan en 0
        }

        return reporte;
    }


    public Collection<Ticket> obtenerTicketsPrioritarios() throws GlobalException, NoDataException {
        Collection<Ticket> todosTickets = controladorTicket.listarTodosLosTickets();
        Collection<Ticket> ticketsPrioritarios = new ArrayList<>();

        // Priorizar tickets NUEVOS y EN_PROGRESO
        for (Ticket ticket : todosTickets) {
            if (ticket.getEstado() == Estado.NUEVO || ticket.getEstado() == Estado.EN_PROGRESO) {
                ticketsPrioritarios.add(ticket);
            }
        }

        if (ticketsPrioritarios.isEmpty()) {
            throw new NoDataException("No hay tickets prioritarios en este momento");
        }

        return ticketsPrioritarios;
    }


    public DepartamentoConTickets obtenerDepartamentoConMasTickets()
            throws GlobalException, NoDataException {
        Collection<Departamento> departamentos = controladorDepartamento.listarDepartamentos();
        Map<String, Integer> conteoTickets = new HashMap<>();

        // Contar tickets por departamento
        for (Departamento depto : departamentos) {
            try {
                Collection<Ticket> tickets = controladorTicket.consultarTicketsPorDepartamento(depto.getId());
                conteoTickets.put(depto.getId(), tickets.size());
            } catch (NoDataException e) {
                conteoTickets.put(depto.getId(), 0);
            }
        }

        // Encontrar el departamento con más tickets
        String idDepartamentoMax = null;
        int maxTickets = 0;

        for (Map.Entry<String, Integer> entry : conteoTickets.entrySet()) {
            if (entry.getValue() > maxTickets) {
                maxTickets = entry.getValue();
                idDepartamentoMax = entry.getKey();
            }
        }

        if (idDepartamentoMax == null) {
            throw new NoDataException("No hay departamentos con tickets");
        }

        // Buscar información del departamento
        Departamento departamento = null;
        for (Departamento depto : departamentos) {
            if (depto.getId().equals(idDepartamentoMax)) {
                departamento = depto;
                break;
            }
        }

        return new DepartamentoConTickets(departamento, maxTickets);
    }


    public UsuarioConTickets obtenerUsuarioConMasTickets() throws GlobalException, NoDataException {
        Collection<Usuario> usuarios = controladorUsuario.listarUsuarios();
        Map<String, Integer> conteoTickets = new HashMap<>();

        // Contar tickets por usuario
        for (Usuario usuario : usuarios) {
            try {
                Collection<Ticket> tickets = controladorTicket.consultarTicketsPorUsuario(usuario.getCedula());
                conteoTickets.put(usuario.getCedula(), tickets.size());
            } catch (NoDataException e) {
                conteoTickets.put(usuario.getCedula(), 0);
            }
        }

        // Encontrar el usuario con más tickets
        String cedulaUsuarioMax = null;
        int maxTickets = 0;

        for (Map.Entry<String, Integer> entry : conteoTickets.entrySet()) {
            if (entry.getValue() > maxTickets) {
                maxTickets = entry.getValue();
                cedulaUsuarioMax = entry.getKey();
            }
        }

        if (cedulaUsuarioMax == null) {
            throw new NoDataException("No hay usuarios con tickets");
        }

        // Buscar información del usuario
        Usuario usuario = controladorUsuario.buscarUsuario(cedulaUsuarioMax);

        return new UsuarioConTickets(usuario, maxTickets);
    }


    public Collection<Ticket> buscarTicketsPorPalabraClave(String palabraClave)
            throws GlobalException, NoDataException {
        if (palabraClave == null || palabraClave.trim().isEmpty()) {
            throw new IllegalArgumentException("La palabra clave no puede estar vacía");
        }

        Collection<Ticket> todosTickets = controladorTicket.listarTodosLosTickets();
        Collection<Ticket> ticketsEncontrados = new ArrayList<>();

        String palabraClaveMinuscula = palabraClave.toLowerCase();

        for (Ticket ticket : todosTickets) {
            if (ticket.getAsunto().toLowerCase().contains(palabraClaveMinuscula) ||
                    ticket.getDescripcion().toLowerCase().contains(palabraClaveMinuscula)) {
                ticketsEncontrados.add(ticket);
            }
        }

        if (ticketsEncontrados.isEmpty()) {
            throw new NoDataException("No se encontraron tickets con esa palabra clave");
        }

        return ticketsEncontrados;
    }


    public void reasignarTicket(String idTicket, String nuevoIdDepartamento)
            throws GlobalException, NoDataException {
        // Verificar que el ticket existe
        Ticket ticket = controladorTicket.buscarTicket(idTicket);

        // Verificar que el nuevo departamento existe
        Departamento nuevoDepartamento = controladorDepartamento.buscarDepartamento(nuevoIdDepartamento);

        if (ticket == null || nuevoDepartamento == null) {
            throw new NoDataException("Ticket o departamento no encontrado");
        }

        // Nota: Necesitarías un método en ServicioTicket para actualizar el departamento
        // Por ahora, esto es una referencia de lo que deberías implementar
        throw new UnsupportedOperationException("Función de reasignación pendiente de implementar en ServicioTicket");
    }


    public ValidacionSistema validarIntegridadSistema() throws GlobalException, NoDataException {
        ValidacionSistema validacion = new ValidacionSistema();

        try {
            // Verificar tickets con usuarios inexistentes
            Collection<Ticket> tickets = controladorTicket.listarTodosLosTickets();
            Collection<Usuario> usuarios = controladorUsuario.listarUsuarios();

            for (Ticket ticket : tickets) {
                boolean usuarioExiste = false;
                for (Usuario usuario : usuarios) {
                    if (usuario.getCedula().equals(ticket.getUsuario().getCedula())) {
                        usuarioExiste = true;
                        break;
                    }
                }
                if (!usuarioExiste) {
                    validacion.ticketsHuerfanos++;
                }
            }

            validacion.esValido = validacion.ticketsHuerfanos == 0;

        } catch (NoDataException e) {
            validacion.esValido = true; // Sin datos, el sistema está técnicamente válido
        }

        return validacion;
    }

    // ==================== CLASES AUXILIARES ====================


    public static class ReporteSistema {
        public int totalTickets = 0;
        public int ticketsNuevos = 0;
        public int ticketsEnProgreso = 0;
        public int ticketsResueltos = 0;
        public int ticketsCerrados = 0;
        public int totalEstudiantes = 0;
        public int totalFuncionarios = 0;
        public int totalAdministradores = 0;
        public int totalDepartamentos = 0;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("╔══════════════════════════════════════════╗\n");
            sb.append("║     REPORTE DEL SISTEMA DE TICKETS       ║\n");
            sb.append("╠══════════════════════════════════════════╣\n");
            sb.append(String.format("║ Total Tickets:          %15d ║\n", totalTickets));
            sb.append(String.format("║   • Nuevos:             %15d ║\n", ticketsNuevos));
            sb.append(String.format("║   • En Progreso:        %15d ║\n", ticketsEnProgreso));
            sb.append(String.format("║   • Resueltos:          %15d ║\n", ticketsResueltos));
            sb.append(String.format("║   • Cerrados:           %15d ║\n", ticketsCerrados));
            sb.append("╠══════════════════════════════════════════╣\n");
            sb.append(String.format("║ Total Usuarios:         %15d ║\n",
                    totalEstudiantes + totalFuncionarios + totalAdministradores));
            sb.append(String.format("║   • Estudiantes:        %15d ║\n", totalEstudiantes));
            sb.append(String.format("║   • Funcionarios:       %15d ║\n", totalFuncionarios));
            sb.append(String.format("║   • Administradores:    %15d ║\n", totalAdministradores));
            sb.append("╠══════════════════════════════════════════╣\n");
            sb.append(String.format("║ Total Departamentos:    %15d ║\n", totalDepartamentos));
            sb.append("╚══════════════════════════════════════════╝");
            return sb.toString();
        }
    }


    public static class DepartamentoConTickets {
        public Departamento departamento;
        public int cantidadTickets;

        public DepartamentoConTickets(Departamento departamento, int cantidadTickets) {
            this.departamento = departamento;
            this.cantidadTickets = cantidadTickets;
        }

        @Override
        public String toString() {
            return String.format("Departamento: %s | Tickets: %d",
                    departamento.getNombre(), cantidadTickets);
        }
    }


    public static class UsuarioConTickets {
        public Usuario usuario;
        public int cantidadTickets;

        public UsuarioConTickets(Usuario usuario, int cantidadTickets) {
            this.usuario = usuario;
            this.cantidadTickets = cantidadTickets;
        }

        @Override
        public String toString() {
            return String.format("Usuario: %s %s | Tickets: %d",
                    usuario.getNombre(), usuario.getApellido1(), cantidadTickets);
        }
    }


    public static class ValidacionSistema {
        public boolean esValido = true;
        public int ticketsHuerfanos = 0;

        @Override
        public String toString() {
            if (esValido) {
                return "✓ Sistema validado correctamente";
            } else {
                return String.format("✗ Se encontraron %d tickets sin usuario válido", ticketsHuerfanos);
            }
        }
    }
}