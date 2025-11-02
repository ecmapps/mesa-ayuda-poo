/**
 * Representa un Departamento de la organización.
 * Basado en el diagrama UML "UML Avance 1 POO.jpg".
 */
public class Departamento {

    // --- Atributos ---
    private String id;
    private String nombre;
    private String descripcion;
    private String correoElectronico;
    private String extensionTel;

    // --- Constructores --- Pendiente integrar a la DB
    public void registrarDepartamento() {
        System.out.println("Registrando departamento: " + this.nombre);
    }

    public void actualizarDepartamento() {
        System.out.println("Actualizando departamento: " + this.id);
    }

    public void eliminarDepartamento() {
        System.out.println("Eliminando departamento: " + this.id);
    }

    // --- Getters y Setters ---

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
}