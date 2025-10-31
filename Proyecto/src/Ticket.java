public class Ticket {
    private String id;
    private String asunto;
    private String descripcion;
    private Estado estado;
    private Usuario usuario;

    public Ticket() {
        id = "";
        asunto = "";
        descripcion = "";
    }


    public Ticket(String id, String asunto, String descripcion, Estado estado, Usuario usuario) {
        this.id = id;
        this.asunto = asunto;
        this.descripcion = descripcion;
        this.estado = estado;
        this.usuario = usuario;
    }


    public void crearTicket() {
        System.out.println("Ticket " + id + " creado exitosamente");
        // Agregar el ticket a la lista del usuario automáticamente
        if (usuario != null) {
            usuario.agregarTicket(this);
        }
    }

    public void actualizarEstado(Estado nuevoEstado) {
        this.estado = nuevoEstado;
        System.out.println("Estado del ticket " + id + " actualizado a " + nuevoEstado);
    }

    public void verDetalle() {
        System.out.println("Detalles del Ticket:");
        System.out.println("ID: " + id);
        System.out.println("Asunto: " + asunto);
        System.out.println("Descripción: " + descripcion);
        System.out.println("Estado: " + estado);
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

}
