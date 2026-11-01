package json;

/**
 * Lector de JSON 
 * Entiende objetos, arreglos, textos y números enteros, que es todo lo que
 * necesita este proyecto. Recorre el texto carácter por carácter.
 */
public class JsonLector {

    private final String texto;
    private int pos;

    /**
     * @param texto contenido completo del archivo JSON
     */
    public JsonLector(String texto) {
        this.texto = texto;
        this.pos = 0;
    }

    /**
     * Lee el JSON completo.
     *
     * @return el valor raíz (normalmente un {@link JsonObjeto})
     * @throws IllegalStateException si el JSON está mal formado
     */
    public Object leer() {
        Object valor = leerValor();
        saltarEspacios();
        if (pos != texto.length()) {
            throw error("Hay texto sobrante después del JSON");
        }
        return valor;
    }

    private Object leerValor() {
        saltarEspacios();
        if (pos >= texto.length()) {
            throw error("El JSON terminó antes de tiempo");
        }
        char c = texto.charAt(pos);
        if (c == '{') {
            return leerObjeto();
        } else if (c == '[') {
            return leerArreglo();
        } else if (c == '"') {
            return leerCadena();
        } else if (c == '-' || Character.isDigit(c)) {
            return leerNumero();
        }
        throw error("Carácter inesperado '" + c + "'");
    }

    private JsonObjeto leerObjeto() {
        JsonObjeto obj = new JsonObjeto();
        esperar('{');
        saltarEspacios();
        if (miraA('}')) {
            pos++;
            return obj;
        }
        while (true) {
            saltarEspacios();
            String clave = leerCadena();
            saltarEspacios();
            esperar(':');
            Object valor = leerValor();
            obj.poner(clave, valor);
            saltarEspacios();
            if (miraA(',')) {
                pos++;
            } else {
                esperar('}');
                return obj;
            }
        }
    }

    private Object[] leerArreglo() {
        Object[] items = new Object[4];
        int cantidad = 0;
        esperar('[');
        saltarEspacios();
        if (miraA(']')) {
            pos++;
            return new Object[0];
        }
        while (true) {
            Object valor = leerValor();
            if (cantidad == items.length) {
                Object[] nuevo = new Object[items.length * 2];
                for (int i = 0; i < cantidad; i++) {
                    nuevo[i] = items[i];
                }
                items = nuevo;
            }
            items[cantidad] = valor;
            cantidad++;
            saltarEspacios();
            if (miraA(',')) {
                pos++;
            } else {
                esperar(']');
                break;
            }
        }
        Object[] resultado = new Object[cantidad];   // se devuelve del tamaño exacto
        for (int i = 0; i < cantidad; i++) {
            resultado[i] = items[i];
        }
        return resultado;
    }

    private String leerCadena() {
        esperar('"');
        StringBuilder sb = new StringBuilder();
        while (pos < texto.length() && texto.charAt(pos) != '"') {
            char c = texto.charAt(pos);
            if (c == '\\') {
                pos++;
                char e = texto.charAt(pos);
                if (e == 'n') {
                    sb.append('\n');
                } else if (e == 't') {
                    sb.append('\t');
                } else if (e == 'u') {
                    sb.append((char) Integer.parseInt(texto.substring(pos + 1, pos + 5), 16));
                    pos += 4;
                } else {
                    sb.append(e);   // \" \\ \/
                }
            } else {
                sb.append(c);
            }
            pos++;
        }
        esperar('"');
        return sb.toString();
    }

    private Integer leerNumero() {
        int inicio = pos;
        if (miraA('-')) {
            pos++;
        }
        while (pos < texto.length() && Character.isDigit(texto.charAt(pos))) {
            pos++;
        }
        return Integer.valueOf(texto.substring(inicio, pos));
    }

    private void saltarEspacios() {
        while (pos < texto.length() && Character.isWhitespace(texto.charAt(pos))) {
            pos++;
        }
    }

    private boolean miraA(char c) {
        return pos < texto.length() && texto.charAt(pos) == c;
    }

    private void esperar(char c) {
        if (!miraA(c)) {
            throw error("Se esperaba '" + c + "'");
        }
        pos++;
    }

    private IllegalStateException error(String mensaje) {
        return new IllegalStateException("JSON inválido en la posición " + pos + ": " + mensaje);
    }
}