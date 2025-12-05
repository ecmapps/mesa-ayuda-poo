package diccionario;

import java.util.ArrayList;

public class Diccionario {

    protected ArrayList<PalabraDiccionario> palabras;

    public Diccionario() {
        this.palabras = new ArrayList<PalabraDiccionario>();
    }

    public void agregarPalabra(PalabraDiccionario palabra) {
        if (palabra != null) {
            palabras.add(palabra);
        }
    }

    public void eliminarPalabra(String textoPalabra) {
        if (textoPalabra == null) return;

        for (int i = palabras.size() - 1; i >= 0; i--) {
            PalabraDiccionario p = palabras.get(i);
            if (p.getPalabra().equalsIgnoreCase(textoPalabra)) {
                palabras.remove(i);
            }
        }
    }

    public void actualizarPalabra(PalabraDiccionario nueva) {
        if (nueva == null) return;
        eliminarPalabra(nueva.getPalabra());
        agregarPalabra(nueva);
    }

    protected PalabraDiccionario buscarPalabra(String token) {
        if (token == null || token.isEmpty()) return null;

        for (PalabraDiccionario p : palabras) {
            if (p.getPalabra().equalsIgnoreCase(token)) {
                return p;
            }
        }
        return null;
    }
    protected String[] tokenizar(String texto) {
        if (texto == null || texto.isEmpty()) {
            return new String[0];
        }

        texto = texto.toLowerCase();
        texto = texto.replace(",", " ")
                .replace(".", " ")
                .replace(";", " ")
                .replace(":", " ")
                .replace("?", " ")
                .replace("!", " ");

        return texto.trim().split("\\s+");
    }
}