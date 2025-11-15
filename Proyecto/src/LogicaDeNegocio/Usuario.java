package LogicaDeNegocio;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
        private String cedula;
        private String nombre;
        private String apellido1;
        private String apellido2;
        private String correoElectronico;
        private String contrasenna;
        private String numTelefono;
        private Rol rol;
        private List<Ticket> misTickets;


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
        this.misTickets = new ArrayList<>();
    }



    public boolean registrar() {
        System.out.println("LogicaDeNegocio.Usuario " + nombre + " " + apellido1 + " registrado exitosamente");
        return true;
    }

    public boolean iniciarSesion() {
        System.out.println("LogicaDeNegocio.Usuario " + correoElectronico + " ha iniciado sesión");
        return true;
    }

    public String encriptarContrasenna() {
        return "encrypted_" + contrasenna;
    }

    public void agregarTicket(Ticket ticket) {
        misTickets.add(ticket);
        System.out.println("LogicaDeNegocio.Ticket " + ticket.getId() + " agregado a la lista del usuario");
    }

    public void verMisTickets() {
        System.out.println("\n=== Tickets de " + nombre + " " + apellido1 + " ===");
        if (misTickets.isEmpty()) {
            System.out.println("No tienes tickets creados");
        } else {
            for (Ticket ticket : misTickets) {
                System.out.println("- ID: " + ticket.getId() + " | Asunto: " + ticket.getAsunto() + " | LogicaDeNegocio.Estado: " + ticket.getEstado());
            }
        }
    }


//    GETTERS

    public List<Ticket> getMisTickets() {
        return misTickets;
    }
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
        return "LogicaDeNegocio.Usuario{" +
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

