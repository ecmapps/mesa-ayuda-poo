package Controlador;

import AccesoADatos.*;
import LogicaDeNegocio.Departamento;
import java.util.Collection;


public class ControladorDepartamento {

    private ServicioDepartamento servicioDepartamento;

    public ControladorDepartamento() {
        this.servicioDepartamento = new ServicioDepartamento();
    }


    public void registrarDepartamento(String id, String nombre, String descripcion,
                                      String correo, String extension)
            throws GlobalException, NoDataException {

        // Validaciones de negocio
        validarDatosDepartamento(id, nombre);

        // Crear objeto Departamento
        Departamento departamento = new Departamento(id, nombre, descripcion, correo, extension);

        // Insertar en la base de datos
        servicioDepartamento.registrarDepartamento(departamento);
    }


    public void actualizarDepartamento(String id, String nombre, String descripcion,
                                       String correo, String extension)
            throws GlobalException, NoDataException {

        // Validaciones de negocio
        validarDatosDepartamento(id, nombre);

        // Verificar que el departamento existe
        if (!existeDepartamento(id)) {
            throw new NoDataException("El departamento no existe en el sistema");
        }

        // Crear objeto Departamento con datos actualizados
        Departamento departamento = new Departamento(id, nombre, descripcion, correo, extension);

        // Actualizar en la base de datos
        servicioDepartamento.actualizarDepartamento(departamento);
    }

    public void eliminarDepartamento(String id) throws GlobalException, NoDataException {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del departamento no puede estar vacío");
        }

        // Verificar que el departamento existe antes de eliminar
        if (!existeDepartamento(id)) {
            throw new NoDataException("El departamento no existe en el sistema");
        }

        // Eliminar de la base de datos
        servicioDepartamento.eliminarDepartamento(id);
    }


    public Departamento buscarDepartamento(String id) throws GlobalException, NoDataException {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del departamento no puede estar vacío");
        }

        return servicioDepartamento.buscarDepartamento(id);
    }


    public Collection<Departamento> listarDepartamentos() throws GlobalException, NoDataException {
        return servicioDepartamento.listarDepartamentos();
    }


    public boolean existeDepartamento(String id) throws GlobalException, NoDataException {
        try {
            servicioDepartamento.buscarDepartamento(id);
            return true;
        } catch (NoDataException e) {
            return false;
        }
    }

    private void validarDatosDepartamento(String id, String nombre) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del departamento es obligatorio");
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del departamento es obligatorio");
        }

        // Validar que el ID no contenga espacios
        if (id.contains(" ")) {
            throw new IllegalArgumentException("El ID no puede contener espacios");
        }

        // Validar longitud del ID
        if (id.length() > 20) {
            throw new IllegalArgumentException("El ID no puede tener más de 20 caracteres");
        }
    }


    public boolean validarCorreo(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            return true; // El correo es opcional
        }
        return correo.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }


    public int contarDepartamentos() throws GlobalException, NoDataException {
        try {
            Collection<Departamento> departamentos = servicioDepartamento.listarDepartamentos();
            return departamentos.size();
        } catch (NoDataException e) {
            return 0;
        }
    }


    public Collection<Departamento> buscarPorNombre(String nombreParcial)
            throws GlobalException, NoDataException {
        Collection<Departamento> todosDepartamentos = servicioDepartamento.listarDepartamentos();
        Collection<Departamento> resultado = new java.util.ArrayList<>();

        for (Departamento depto : todosDepartamentos) {
            if (depto.getNombre().toLowerCase().contains(nombreParcial.toLowerCase())) {
                resultado.add(depto);
            }
        }

        if (resultado.isEmpty()) {
            throw new NoDataException("No se encontraron departamentos con ese nombre");
        }

        return resultado;
    }
}