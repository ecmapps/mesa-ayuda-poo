package LogicaDeNegocio;

public class Ticket {
    private String id;
    private String asunto;
    private String descripcion;
    private Estado estado;
    private Usuario usuario;
    private Departamento departamento; // New attribute

    public Ticket() {
        id = "";
        asunto = "";
        descripcion = "";
    }

    public Ticket(String id, String asunto, String descripcion, Estado estado, Usuario usuario, Departamento departamento) {
        this.id = id;
        this.asunto = asunto;
        this.descripcion = descripcion;
        this.estado = estado;
        this.usuario = usuario;
        this.departamento = departamento; // Extendiendo la clase con departamento
    }

    public void crearTicket() {
        System.out.println("LogicaDeNegocio.Ticket " + id + " creado exitosamente");
        // Agregar el ticket a la lista del usuario automáticamente
        if (usuario != null) {
            usuario.agregarTicket(this);
        }
    }

    public void actualizarEstado(Estado nuevoEstado) {
        this.estado = nuevoEstado;
        System.out.println("LogicaDeNegocio.Estado del ticket " + id + " actualizado a " + nuevoEstado);
    }

    public void verDetalle() {
        System.out.println("\nDetalles del LogicaDeNegocio.Ticket:");
        System.out.println("ID: " + id);
        System.out.println("Asunto: " + asunto);
        System.out.println("Descripción: " + descripcion);
        System.out.println("LogicaDeNegocio.Estado: " + estado + "\n");
    }

//    GETTERS
    public String getId() {
        return id;
    }

    public String getAsunto() {
        return asunto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Estado getEstado() {
        return estado;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public Usuario getUsuario() {
        return usuario;
    }

//    SETTERS
    public void setId(String id) {
        this.id = id;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n┌─────────────────────────────────────────────────────────────┐\n");
        sb.append("│ TICKET: ").append(id).append("\n");
        sb.append("├─────────────────────────────────────────────────────────────┤\n");
        sb.append("│ Asunto: ").append(asunto).append("\n");
        sb.append("│ Descripción: ").append(descripcion).append("\n");
        sb.append("│ Estado: ").append(estado).append("\n");
        sb.append("│ Usuario: ").append(usuario != null ? usuario.getCedula() : "N/A").append("\n");
        sb.append("│ Departamento: ").append(departamento != null ? departamento.getId() : "N/A").append("\n");
        sb.append("└─────────────────────────────────────────────────────────────┘");
        return sb.toString();
    }
}
