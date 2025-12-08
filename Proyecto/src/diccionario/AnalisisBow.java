package diccionario;

import LogicaDeNegocio.Ticket;
import AccesoADatos.GlobalException;
import AccesoADatos.NoDataException;
import AccesoADatos.ServicioDiccionarioEmocional;
import AccesoADatos.ServicioDiccionarioTecnico;

import java.util.Collection;

public class AnalisisBow {

    private String estadoAnimo;
    private String categoriaSugerida;

    private ServicioDiccionarioEmocional servicioDiccionarioEmocional;
    private ServicioDiccionarioTecnico servicioDiccionarioTecnico;

    public AnalisisBow(ServicioDiccionarioEmocional servicioDiccionarioEmocional, ServicioDiccionarioTecnico servicioDiccionarioTecnico) {
        this.servicioDiccionarioEmocional = servicioDiccionarioEmocional;
        this.servicioDiccionarioTecnico = servicioDiccionarioTecnico;
    }

    public void analizarPalabras(Ticket ticket) {
        System.out.println("Analizando Ticket: " + ticket.getId());
        String descripcion = ticket.getDescripcion();

        try {
            // Detecta emoción usando ServicioDiccionarioEmocional
            estadoAnimo = detectarEmocion(descripcion);

            // Suguiere categoría técnica usando ServicioDiccionarioTecnico
            categoriaSugerida = sugerirCategoriaTecnica(descripcion);

            mostrarResultados();
        } catch (GlobalException | NoDataException e) {
            System.err.println("Error durante el análisis: " + e.getMessage());
        }
    }

    private String detectarEmocion(String descripcion) throws GlobalException, NoDataException {
        Collection<PalabraDiccionario> palabrasEmocionales = servicioDiccionarioEmocional.listarTodos();
        for (PalabraDiccionario palabra : palabrasEmocionales) {
            if (descripcion.contains(palabra.getPalabra())) {
                return palabra.getCategoria(); // Retorna la categoría emocional asociada
            }
        }
        return "Neutral"; // Si no se encuentra ninguna palabra emocional
    }

    private String sugerirCategoriaTecnica(String descripcion) throws GlobalException, NoDataException {
        Collection<PalabraDiccionario> palabrasTecnicas = servicioDiccionarioTecnico.listarTodos();
        for (PalabraDiccionario palabra : palabrasTecnicas) {
            if (descripcion.contains(palabra.getPalabra())) {
                return palabra.getCategoria(); // Retorna la categoría técnica asociada
            }
        }
        return "General"; // Si no se encuentra ninguna palabra técnica
    }

    public void mostrarResultados() {
        System.out.println("--- Resultados del Análisis ---");
        System.out.println("Estado de Ánimo: " + estadoAnimo);
        System.out.println("Categoría Sugerida: " + categoriaSugerida);
    }

    // Getters
    public String getEstadoAnimo() {
        return estadoAnimo;
    }

    public String getCategoriaSugerida() {
        return categoriaSugerida;
    }
}