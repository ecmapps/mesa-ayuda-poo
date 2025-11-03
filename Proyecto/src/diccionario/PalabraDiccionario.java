package diccionario;

public class PalabraDiccionario {

    private String palabra;
    private String categoria;

    // Constructor por defecto
    public PalabraDiccionario() {
    }

    public PalabraDiccionario(String palabra, String categoria) {
        setPalabra(palabra);
        setCategoria(categoria);
    }

    public String getPalabra() {
        return palabra;
    }

    public void setPalabra(String palabra) {
        if (palabra == null || palabra.trim().isEmpty()) {
            throw new IllegalArgumentException("La palabra no puede estar vacía.");
        }
        this.palabra = normalizar(palabra);
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("La categoría no puede estar vacía.");
        }
        this.categoria = categoria.trim();
    }

    private static String normalizar(String texto) {
        String t = texto.trim().toLowerCase();
        t = t.replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u");
        return t;
    }

    @Override
    public String toString() {
        return "{" + palabra + " / " + categoria + "}";
    }
}

