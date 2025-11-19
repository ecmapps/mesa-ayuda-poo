package diccionario;

import AccesoADatos.ServicioDiccionarioTecnico;
import AccesoADatos.GlobalException;
import AccesoADatos.NoDataException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DiccionarioTecnico extends Diccionario {

    public DiccionarioTecnico() {
        super();
    }
    //bd
    public void cargarDesdeBD() throws GlobalException, NoDataException {
        ServicioDiccionarioTecnico servicio = new ServicioDiccionarioTecnico();
        Collection<PalabraDiccionario> lista = servicio.listarTodos();

        for (PalabraDiccionario p : lista) {
            this.agregarPalabra(p);
        }
    }

    public void agregarPalabra(PalabraDiccionario palabra) {
        if (palabra != null) {
            palabras.add(palabra);
        }
    }

    public String obtenerCategoriaTecnica(String descripcion) {
        String[] tokens = tokenizar(descripcion);
        if (tokens.length == 0 || palabras.isEmpty()) return null;

        Map<String, Integer> conteo = new HashMap<>();

        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            PalabraDiccionario palabraEncontrada = buscarPalabra(token);
            if (palabraEncontrada != null) {
                String categoria = palabraEncontrada.getCategoria();
                if (!conteo.containsKey(categoria)) {
                    conteo.put(categoria, 1);
                } else {
                    int nuevoValor = conteo.get(categoria) + 1;
                    conteo.put(categoria, nuevoValor);
                }
            }
        }
        if (conteo.isEmpty()) return null;

        String categoriaMayor = null;
        int max = -1;
        for (Map.Entry<String, Integer> entry : conteo.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                categoriaMayor = entry.getKey();
            }
        }
        return categoriaMayor;
    }
}
