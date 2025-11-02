package diccionario;

import javax.swing.JOptionPane;

public class main {

    public static void main(String[] args) {
        DiccionarioTecnico dicTec = new DiccionarioTecnico();
        DiccionarioEmocional dicEmo = new DiccionarioEmocional();

        dicTec.agregarPalabra("impresora", "Impresoras");
        dicTec.agregarPalabra("wifi", "Redes");
        dicTec.agregarPalabra("conexion", "Redes");
        dicTec.agregarPalabra("credenciales", "Cuentas");

        dicEmo.agregarPalabra("enojado", "Frustracion");
        dicEmo.agregarPalabra("triste", "Deprimido");
        dicEmo.agregarPalabra("divertido", "Extrovertido");

        String[] textos = {
                "Estoy super enojado, la impresora no funciona.",
                "No se puedo conectarme al wifi, ayuda urgente",
                "Olvidé mis credenciales y no puedo entrar al sistema.",
                "Estoy triste porque la impresora no imprime.",
                "Hoy todo funciona perfecto, estoy divertido y feliz."
        };

        StringBuilder resultados = new StringBuilder();
        resultados.append("Caso prueba\n");

        for (int i = 0; i < textos.length; i++) {
            String texto = textos[i];
            String emocion = dicEmo.detectarEmocion(texto);
            String categoria = dicTec.obtenerCategoriaTecnica(texto);

            if (emocion == null) emocion = "No detectada";
            if (categoria == null) categoria = "No detectada";

            resultados.append("Caso #").append(i + 1).append("\n");
            resultados.append("Texto: ").append(texto).append("\n");
            resultados.append("Emoción: ").append(emocion).append("\n");
            resultados.append("Categoría: ").append(categoria).append("\n");
        }

        JOptionPane.showMessageDialog(null, resultados.toString(), "Casos de Prueba Automáticos", JOptionPane.INFORMATION_MESSAGE);

        boolean salir = false;

        while (!salir) {
            String menu = """
                    Emociones:
                    1. Agregar palabra TÉCNICA
                    2. Agregar palabra EMOCIONAL
                    3. Eliminar palabra
                    4. Listar diccionarios
                    5. Analizar texto
                    0. Salir
                    """;

            String opcion = JOptionPane.showInputDialog(null, menu, "Menú principal", JOptionPane.QUESTION_MESSAGE);
            if (opcion == null) break;

            switch (opcion.trim()) {
                case "1":
                    agregarPalabra(dicTec, "técnica");
                    break;
                case "2":
                    agregarPalabra(dicEmo, "emocional");
                    break;
                case "3":
                    eliminarPalabra(dicTec, dicEmo);
                    break;
                case "4":
                    listarDiccionarios(dicTec, dicEmo);
                    break;
                case "5":
                    analizarTexto(dicTec, dicEmo);
                    break;
                case "0":
                    salir = true;
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida.");
            }
        }

        JOptionPane.showMessageDialog(null, "Programa finalizado.");
    }

    private static void agregarPalabra(Diccionario dic, String tipo) {
        String palabra = JOptionPane.showInputDialog("Ingrese la palabra " + tipo + ":");
        if (palabra == null || palabra.trim().isEmpty()) return;

        String categoria = JOptionPane.showInputDialog("Ingrese la categoría para esa palabra:");
        if (categoria == null || categoria.trim().isEmpty()) return;

        boolean ok = dic.agregarPalabra(palabra, categoria);
        if (ok)
            JOptionPane.showMessageDialog(null, "Palabra agregada correctamente.");
        else
            JOptionPane.showMessageDialog(null, "La palabra ya existe en el diccionario.");
    }

    private static void eliminarPalabra(DiccionarioTecnico dicTec, DiccionarioEmocional dicEmo) {
        String tipo = JOptionPane.showInputDialog("(T)écnico o (E)mocional?");
        if (tipo == null) return;

        String palabra = JOptionPane.showInputDialog("Ingrese la palabra que desea eliminar:");
        if (palabra == null || palabra.trim().isEmpty()) return;

        boolean ok = tipo.equalsIgnoreCase("T") ? dicTec.eliminarPalabra(palabra) : dicEmo.eliminarPalabra(palabra);

        if (ok)
            JOptionPane.showMessageDialog(null, "Palabra eliminada correctamente.");
        else
            JOptionPane.showMessageDialog(null, "No se encontró la palabra.");
    }

    private static void listarDiccionarios(DiccionarioTecnico dicTec, DiccionarioEmocional dicEmo) {
        StringBuilder sb = new StringBuilder();
        sb.append("DICCIONARIO TÉCNICO\n");
        for (PalabraDiccionario p : dicTec.listarPalabras()) {
            sb.append(p.toString()).append("\n");
        }
        sb.append("\nDICCIONARIO EMOCIONAL\n");
        for (PalabraDiccionario p : dicEmo.listarPalabras()) {
            sb.append(p.toString()).append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private static void analizarTexto(DiccionarioTecnico dicTec, DiccionarioEmocional dicEmo) {
        String texto = JOptionPane.showInputDialog("Ingrese la descripción del ticket a analizar:");
        if (texto == null || texto.trim().isEmpty()) return;

        String emocion = dicEmo.detectarEmocion(texto);
        String categoria = dicTec.obtenerCategoriaTecnica(texto);

        if (emocion == null) emocion = "No detectada";
        if (categoria == null) categoria = "No detectada";

        String resultado = "Texto analizado:\n" + texto + "\n\n" +
                "Emoción detectada: " + emocion + "\n" +
                "Categoría técnica: " + categoria;

        JOptionPane.showMessageDialog(null, resultado);
    }
}
