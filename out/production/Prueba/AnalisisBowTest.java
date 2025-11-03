import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AnalisisBowTest {

    @Test
    void testAnalizarPalabras() {
        // Crear un ticket de prueba
        Departamento departamento = new Departamento();
        departamento.setId("DEP001");
        departamento.setNombre("Soporte Técnico");

        Usuario usuario = new Usuario(
                "123456789",
                "Juan",
                "Pérez",
                "García",
                "juan.perez@email.com",
                "password123",
                "88888888",
                Rol.ESTUDIANTE
        );

        Ticket ticket = new Ticket(
                "TK001",
                "Problema con el servidor",
                "El servidor no responde y la conexión es lenta",
                Estado.NUEVO,
                usuario,
                departamento
        );

        // Analizar el ticket
        AnalisisBow analisis = new AnalisisBow();
        analisis.analizarPalabras(ticket);

        // Verificar resultados
        assertNotNull(analisis.getEstadoAnimo());
        assertNotNull(analisis.getCategoriaSugerida());
    }

    @Test
    void testDetectarAnimo() {
        AnalisisBow analisis = new AnalisisBow();
        analisis.detectarAnimo();
        assertEquals("Frustración", analisis.getEstadoAnimo()); // Basado en el ejemplo actual
    }

    @Test
    void testSugerirCategoriaTecnica() {
        AnalisisBow analisis = new AnalisisBow();
        analisis.sugerirCategoriaTecnica();
        assertEquals("IT", analisis.getCategoriaSugerida()); // Basado en el ejemplo actual
    }
}