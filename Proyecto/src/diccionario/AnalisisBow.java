package diccionario;

import LogicaDeNegocio.Ticket;

public class AnalisisBow {

    private String estadoAnimo;
    private String categoriaSugerida;

    // Analisis del texto de un ticket (uso de diccionarios)
    public void analizarPalabras(Ticket ticket) {
        System.out.println("Analizando LogicaDeNegocio.Ticket: " + ticket.getId());
        detectarAnimo();
        sugerirCategoriaTecnica();
        mostrarResultados();
        System.out.println("Análisis completado.");
    }

    // Deteccion de animo..
    public void detectarAnimo() {
        System.out.println("Detectando ánimo...");
        this.estadoAnimo = "Frustración"; // Ejemplo
    }

    //Diccionario Tecnico para la decision de categoria.. pendiente
    public void sugerirCategoriaTecnica() {
        this.categoriaSugerida = "IT"; // Ejemplo
        System.out.println("Categoria sugerida: " + categoriaSugerida);
    }

    //Consola por ahora para los resultados
    public void mostrarResultados() {
        System.out.println("--- Resultados del Análisis ---");
        System.out.println("LogicaDeNegocio.Estado de Ánimo: " + this.estadoAnimo);
        System.out.println("Categoría Sugerida: " + this.categoriaSugerida);
    }

    // Getters y Setters
    public String getEstadoAnimo() {
        return estadoAnimo;
    }

    public void setEstadoAnimo(String estadoAnimo) {
        this.estadoAnimo = estadoAnimo;
    }

    public String getCategoriaSugerida() {
        return categoriaSugerida;
    }

    public void setCategoriaSugerida(String categoriaSugerida) {
        this.categoriaSugerida = categoriaSugerida;
    }
}
