import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DepartamentoTest {

    @Test
    void testSetAndGetId() {
        Departamento departamento = new Departamento();
        departamento.setId("DEP001");
        assertEquals("DEP001", departamento.getId());
    }

    @Test
    void testSetAndGetNombre() {
        Departamento departamento = new Departamento();
        departamento.setNombre("Soporte Técnico");
        assertEquals("Soporte Técnico", departamento.getNombre());
    }

    @Test
    void testSetAndGetDescripcion() {
        Departamento departamento = new Departamento();
        departamento.setDescripcion("Departamento encargado de soporte técnico.");
        assertEquals("Departamento encargado de soporte técnico.", departamento.getDescripcion());
    }

    @Test
    void testSetAndGetCorreoElectronico() {
        Departamento departamento = new Departamento();
        departamento.setCorreoElectronico("soporte@cenfo.com");
        assertEquals("soporte@empresa.com", departamento.getCorreoElectronico());
    }

    @Test
    void testSetAndGetExtensionTel() {
        Departamento departamento = new Departamento();
        departamento.setExtensionTel("1234");
        assertEquals("1234", departamento.getExtensionTel());
    }

    @Test
    void testRegistrarDepartamento() {
        Departamento departamento = new Departamento();
        departamento.setNombre("Soporte Técnico");
        departamento.registrarDepartamento();
    }

    @Test
    void testActualizarDepartamento() {
        Departamento departamento = new Departamento();
        departamento.setId("DEP003");
        departamento.actualizarDepartamento();
    }

    @Test
    void testEliminarDepartamento() {
        Departamento departamento = new Departamento();
        departamento.setId("DEP003");
        departamento.eliminarDepartamento();
    }
}