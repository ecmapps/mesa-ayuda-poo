public class Usuario {
        private String cedula;
        private String nombre;
        private String apellido1;
        private String apellido2;
        private String correoElectronico;
        private String contrasenna;
        private String numTelefono;
        private Rol rol;


        public Usuario(){
        cedula = "";
        nombre = "";
        apellido1 = "";
        apellido2 = "";
        correoElectronico = "";
        contrasenna = "";
        numTelefono = "";
        }

    public Usuario(String cedula, String nombre, String apellido1, String apellido2,
                   String correoElectronico, String contrasenna, String numTelefono, Rol rol) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.correoElectronico = correoElectronico;
        this.contrasenna = contrasenna;
        this.numTelefono = numTelefono;
        this.rol = rol;
    }

    public boolean registrar() {
        System.out.println("Usuario " + nombre + " " + apellido1 + " registrado exitosamente");
        return true;
    }

    public boolean iniciarSesion() {
        System.out.println("Usuario " + correoElectronico + " ha iniciado sesión");
        return true;
    }

    public String encriptarContrasenna() {
        return "encrypted_" + contrasenna;
    }

//    GETTERS
    public String getCedula() {
        return cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public String getContrasenna() {
        return contrasenna;
    }

    public String getNumTelefono() {
        return numTelefono;
    }

    public Rol getRol() {
        return rol;
    }

//    SETTERS

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public void setContrasenna(String contrasenna) {
        this.contrasenna = contrasenna;
    }

    public void setNumTelefono(String numTelefono) {
        this.numTelefono = numTelefono;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "cedula='" + cedula + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido1='" + apellido1 + '\'' +
                ", apellido2='" + apellido2 + '\'' +
                ", correoElectronico='" + correoElectronico + '\'' +
                ", contrasenna='" + contrasenna + '\'' +
                ", numTelefono='" + numTelefono + '\'' +
                ", rol=" + rol +
                '}';
    }


    }

