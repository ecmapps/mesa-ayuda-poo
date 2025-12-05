package Controlador;

import AccesoADatos.*;
import LogicaDeNegocio.*;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Date;


public class ControladorTicket {

    private ServicioTicket servicioTicket;
    private ServicioUsuario servicioUsuario;
    private ServicioDepartamento servicioDepartamento;

    public ControladorTicket() {
        this.servicioTicket = new ServicioTicket();
        this.servicioUsuario = new ServicioUsuario();
        this.servicioDepartamento = new ServicioDepartamento();
    }


    public void crearTicket(String id, String asunto, String descripcion,
                            String cedulaUsuario, String idDepartamento)
            throws GlobalException, NoDataException {

        // Validaciones de negocio
        validarDatosTicket(id, asunto, cedulaUsuario, idDepartamento);

        // Verificar que el usuario existe
        Usuario usuario = servicioUsuario.buscarUsuario(cedulaUsuario);
        if (usuario == null) {
            throw new NoDataException("El usuario especificado no existe");
        }

        // Verificar que el departamento existe
        Departamento departamento = buscarDepartamentoPorId(idDepartamento);
        if (departamento == null) {
            throw new NoDataException("El departamento especificado no existe");
        }

        // Crear objeto Ticket con estado inicial NUEVO
        Ticket ticket = new Ticket(id, asunto, descripcion, Estado.NUEVO, usuario, departamento);

        // Guardar en la base de datos
        servicioTicket.crearTicket(ticket);
    }


    public void actualizarEstadoTicket(String id, Estado nuevoEstado)
            throws GlobalException, NoDataException {

        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del ticket no puede estar vacío");
        }

        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }

        // Verificar que el ticket existe
        Ticket ticket = servicioTicket.buscarTicket(id);
        if (ticket == null) {
            throw new NoDataException("El ticket no existe en el sistema");
        }

        // Validar transiciones de estado permitidas
        validarTransicionEstado(ticket.getEstado(), nuevoEstado);

        // Actualizar estado en la base de datos
        servicioTicket.actualizarEstado(id, nuevoEstado);
    }


    public Ticket buscarTicket(String id) throws GlobalException, NoDataException {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del ticket no puede estar vacío");
        }

        return servicioTicket.buscarTicket(id);
    }


    public Collection<Ticket> listarTodosLosTickets() throws GlobalException, NoDataException {
        return servicioTicket.listarTickets();
    }



    public Collection<Ticket> consultarTicketsPorUsuario(String cedula)
            throws GlobalException, NoDataException {
        if (cedula == null || cedula.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del usuario no puede estar vacía");
        }

        // Verificar que el usuario existe
        Usuario usuario = servicioUsuario.buscarUsuario(cedula);
        if (usuario == null) {
            throw new NoDataException("El usuario especificado no existe");
        }

        return servicioTicket.consultarTicketsUsuario(cedula);
    }


    public Collection<Ticket> consultarTicketsPorDepartamento(String idDepartamento)
            throws GlobalException, NoDataException {
        if (idDepartamento == null || idDepartamento.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del departamento no puede estar vacío");
        }

        // Verificar que el departamento existe
        Departamento departamento = buscarDepartamentoPorId(idDepartamento);
        if (departamento == null) {
            throw new NoDataException("El departamento especificado no existe");
        }

        return servicioTicket.consultarTicketsDepartamento(idDepartamento);
    }


    public EstadisticasTicket obtenerEstadisticas() throws GlobalException, NoDataException {
        Collection<Ticket> todosTickets = servicioTicket.listarTickets();

        int nuevos = 0;
        int enProgreso = 0;
        int resueltos = 0;
        int cerrados = 0;

        for (Ticket ticket : todosTickets) {
            switch (ticket.getEstado()) {
                case NUEVO:
                    nuevos++;
                    break;
                case EN_PROGRESO:
                    enProgreso++;
                    break;
                case RESUELTO:
                    resueltos++;
                    break;
            }
        }

        return new EstadisticasTicket(todosTickets.size(), nuevos, enProgreso, resueltos, cerrados);
    }


    public Collection<Ticket> filtrarPorEstado(Estado estado) throws GlobalException, NoDataException {
        Collection<Ticket> todosTickets = servicioTicket.listarTickets();
        Collection<Ticket> ticketsFiltrados = new ArrayList<>();

        for (Ticket ticket : todosTickets) {
            if (ticket.getEstado() == estado) {
                ticketsFiltrados.add(ticket);
            }
        }

        if (ticketsFiltrados.isEmpty()) {
            throw new NoDataException("No hay tickets con el estado especificado");
        }

        return ticketsFiltrados;
    }


    public String generarIdTicket() throws GlobalException, NoDataException {
        try {
            Collection<Ticket> tickets = servicioTicket.listarTickets();
            int maxId = 0;

            for (Ticket ticket : tickets) {
                try {
                    String idStr = ticket.getId().replace("TKT", "");
                    int idNum = Integer.parseInt(idStr);
                    if (idNum > maxId) {
                        maxId = idNum;
                    }
                } catch (NumberFormatException e) {
                    // Ignorar IDs con formato diferente
                }
            }

            return String.format("TKT%03d", maxId + 1);
        } catch (NoDataException e) {
            // Si no hay tickets, comenzar desde TKT001
            return "TKT001";
        }
    }


    private void validarDatosTicket(String id, String asunto, String cedulaUsuario, String idDepartamento) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del ticket es obligatorio");
        }

        if (asunto == null || asunto.trim().isEmpty()) {
            throw new IllegalArgumentException("El asunto del ticket es obligatorio");
        }

        if (cedulaUsuario == null || cedulaUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del usuario es obligatoria");
        }

        if (idDepartamento == null || idDepartamento.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del departamento es obligatorio");
        }

        // Validar longitud del asunto
        if (asunto.length() > 200) {
            throw new IllegalArgumentException("El asunto no puede exceder 200 caracteres");
        }
    }


    private void validarTransicionEstado(Estado estadoActual, Estado nuevoEstado) {
        // Reglas de transición de estados
        if (estadoActual == Estado.NUEVO && nuevoEstado == Estado.RESUELTO) {
            throw new IllegalArgumentException("Un ticket nuevo debe pasar primero a 'En Progreso'");
        }

        if (estadoActual == Estado.RESUELTO && nuevoEstado == Estado.NUEVO) {
            throw new IllegalArgumentException("Un ticket resuelto no puede volver a estado 'Nuevo'");
        }
    }


    private Departamento buscarDepartamentoPorId(String id) throws GlobalException, NoDataException {
        Collection<Departamento> departamentos = servicioDepartamento.listarDepartamentos(); //
        for (Departamento depto : departamentos) {
            if (depto.getId().equalsIgnoreCase(id)) {
                return depto;
            }
        }
        return null;
    }


    public static class EstadisticasTicket {
        private int total;
        private int nuevos;
        private int enProgreso;
        private int resueltos;
        private int cerrados;

        public EstadisticasTicket(int total, int nuevos, int enProgreso, int resueltos, int cerrados) {
            this.total = total;
            this.nuevos = nuevos;
            this.enProgreso = enProgreso;
            this.resueltos = resueltos;
            this.cerrados = cerrados;
        }

        public int getTotal() { return total; }
        public int getNuevos() { return nuevos; }
        public int getEnProgreso() { return enProgreso; }
        public int getResueltos() { return resueltos; }
        public int getCerrados() { return cerrados; }

        @Override
        public String toString() {
            return String.format("Total: %d | Nuevos: %d | En Progreso: %d | Resueltos: %d | Cerrados: %d",
                    total, nuevos, enProgreso, resueltos, cerrados);
        }
    }
}