package Controlador;

import AccesoADatos.*;
import LogicaDeNegocio.Usuario;
import LogicaDeNegocio.Rol;
import java.util.Collection;


public class ControladorUsuario {

    private ServicioUsuario servicioUsuario;

    public ControladorUsuario() {
        this.servicioUsuario = new ServicioUsuario();
    }


    public void registrarUsuario(String cedula, String nombre, String apellido1,
                                 String apellido2, String correo, String contrasenna,
                                 String telefono, Rol rol) throws GlobalException, NoDataException {

        // Validaciones de negocio
        validarDatosUsuario(cedula, nombre, correo);

        // Encriptar contraseña (puedes implementar tu propia lógica de encriptación)
        String contrasennaEncriptada = encriptarContrasenna(contrasenna);

        // Crear objeto Usuario
        Usuario usuario = new Usuario(cedula, nombre, apellido1, apellido2,
                correo, contrasennaEncriptada, telefono, rol);

        // Registrar en la base de datos
        servicioUsuario.registrarUsuario(usuario);
    }


    public void actualizarUsuario(String cedula, String nombre, String apellido1,
                                  String apellido2, String correo, String contrasenna,
                                  String telefono, Rol rol) throws GlobalException, NoDataException {

        // Validaciones de negocio
        validarDatosUsuario(cedula, nombre, correo);

        // Verificar que el usuario existe
        Usuario usuarioExistente = servicioUsuario.buscarUsuario(cedula);
        if (usuarioExistente == null) {
            throw new NoDataException("El usuario no existe en el sistema");
        }

        // Si se proporciona nueva contraseña, encriptarla
        String contrasennaFinal = contrasenna;
        if (contrasenna != null && !contrasenna.trim().isEmpty()) {
            contrasennaFinal = encriptarContrasenna(contrasenna);
        } else {
            // Mantener la contraseña anterior
            contrasennaFinal = usuarioExistente.getContrasenna();
        }

        // Crear objeto Usuario con datos actualizados
        Usuario usuario = new Usuario(cedula, nombre, apellido1, apellido2,
                correo, contrasennaFinal, telefono, rol);

        // Actualizar en la base de datos
        servicioUsuario.actualizarUsuario(usuario);
    }

    public void eliminarUsuario(String cedula) throws GlobalException, NoDataException {
        if (cedula == null || cedula.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula no puede estar vacía");
        }

        // Verificar que el usuario existe antes de eliminar
        Usuario usuario = servicioUsuario.buscarUsuario(cedula);
        if (usuario == null) {
            throw new NoDataException("El usuario no existe en el sistema");
        }

        // Eliminar de la base de datos
        servicioUsuario.eliminarUsuario(cedula);
    }


    public Usuario buscarUsuario(String cedula) throws GlobalException, NoDataException {
        if (cedula == null || cedula.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula no puede estar vacía");
        }

        return servicioUsuario.buscarUsuario(cedula);
    }


    public Collection<Usuario> listarUsuarios() throws GlobalException, NoDataException {
        return servicioUsuario.listarUsuarios();
    }


    public Usuario iniciarSesion(String correo, String contrasenna) throws GlobalException, NoDataException {
        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo no puede estar vacío");
        }
        if (contrasenna == null || contrasenna.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }

        // Listar todos los usuarios y buscar por correo
        Collection<Usuario> usuarios = servicioUsuario.listarUsuarios();
        String contrasennaEncriptada = encriptarContrasenna(contrasenna);

        for (Usuario usuario : usuarios) {
            if (usuario.getCorreoElectronico().equalsIgnoreCase(correo)
                    && usuario.getContrasenna().equals(contrasennaEncriptada)) {
                return usuario;
            }
        }

        throw new NoDataException("Credenciales inválidas");
    }


    private void validarDatosUsuario(String cedula, String nombre, String correo) {
        if (cedula == null || cedula.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula es obligatoria");
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo electrónico es obligatorio");
        }

        // Validar formato de correo
        if (!correo.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("El formato del correo electrónico no es válido");
        }

        // Validar formato de cédula (solo números)
        if (!cedula.matches("^[0-9]+$")) {
            throw new IllegalArgumentException("La cédula debe contener solo números");
        }
    }


    private String encriptarContrasenna(String contrasenna) {
        if (contrasenna == null || contrasenna.trim().isEmpty()) {
            return "";
        }
        // Implementación simple - en producción usar BCrypt o similar
        return "encrypted_" + contrasenna;
    }


    public boolean correoExiste(String correo) throws GlobalException, NoDataException {
        try {
            Collection<Usuario> usuarios = servicioUsuario.listarUsuarios();
            for (Usuario usuario : usuarios) {
                if (usuario.getCorreoElectronico().equalsIgnoreCase(correo)) {
                    return true;
                }
            }
            return false;
        } catch (NoDataException e) {
            return false;
        }
    }


    public int contarUsuariosPorRol(Rol rol) throws GlobalException, NoDataException {
        Collection<Usuario> usuarios = servicioUsuario.listarUsuarios();
        int contador = 0;
        for (Usuario usuario : usuarios) {
            if (usuario.getRol() == rol) {
                contador++;
            }
        }
        return contador;
    }
}