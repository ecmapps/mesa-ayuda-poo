package LogicaDeNegocio;

public class Departamento {

    private String id;
    private String nombre;
    private String descripcion;
    private String correoElectronico;
    private String extensionTel;

    // Constructor completo
    public Departamento(String id, String nombre, String descripcion, String correoElectronico, String extensionTel) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.correoElectronico = correoElectronico;
        this.extensionTel = extensionTel;
    }

    // Constructor vacío
    public Departamento() {
        this.id = "";
        this.nombre = "";
        this.descripcion = "";
        this.correoElectronico = "";
        this.extensionTel = "";
    }

    // Constructor simplificado (para compatibilidad con interfaz simple)
    public Departamento(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = "";
        this.correoElectronico = "";
        this.extensionTel = "";
    }

    // Métodos de negocio (Pendiente integrar a la DB - ya se integran mediante servicios)
    public void registrarDepartamento() {
        System.out.println("Registrando departamento: " + this.nombre);
    }

    public void actualizarDepartamento() {
        System.out.println("Actualizando departamento: " + this.id);
    }

    public void eliminarDepartamento() {
        System.out.println("Eliminando departamento: " + this.id);
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getExtensionTel() {
        return extensionTel;
    }

    public void setExtensionTel(String extensionTel) {
        this.extensionTel = extensionTel;
    }

    @Override
    public String toString() {
        return "Departamento{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", correoElectronico='" + correoElectronico + '\'' +
                ", extensionTel='" + extensionTel + '\'' +
                '}';
    }
}