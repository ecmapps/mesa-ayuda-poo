package diccionario;

import java.util.HashMap;
import java.util.Map;

public class DiccionarioEmocional extends Diccionario {

    public DiccionarioEmocional() {
        super();
    }

    public String detectarEmocion(String descripcion) {
        String[] tokens = tokenizar(descripcion);
        if (tokens.length == 0 || palabras.isEmpty()) return null;

        Map<String, Integer> conteo = new HashMap<>();

        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            PalabraDiccionario palabraEncontrada = buscarPalabra(token);
            if (palabraEncontrada != null) {
                String emocion = palabraEncontrada.getCategoria();
                if (!conteo.containsKey(emocion)) {
                    conteo.put(emocion, 1);
                } else {
                    int nuevoValor = conteo.get(emocion) + 1;
                    conteo.put(emocion, nuevoValor);
                }
            }
        }
        if (conteo.isEmpty()) return null;

        String emocionMayor = null;
        int max = -1;
        for (Map.Entry<String, Integer> entry : conteo.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                emocionMayor = entry.getKey();
            }
        }
        return emocionMayor;
    }
}
