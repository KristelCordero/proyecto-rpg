package json;

/**
 * Representa un objeto JSON
 * Guarda las claves y los valores en dos arreglos paralelos
 * Los valores pueden ser String, Integer, JsonObjeto u Object[] (un arreglo JSON).
 */
public class JsonObjeto {

    private String[] claves = new String[8];
    private Object[] valores = new Object[8];
    private int cantidad = 0;

    /**
     * Agrega un par clave-valor, agrandando los arreglos si hace falta
     *
     * @param clave nombre del campo
     * @param valor valor del campo
     */
    public void poner(String clave, Object valor) {
        if (cantidad == claves.length) {
            String[] nuevasClaves = new String[claves.length * 2];
            Object[] nuevosValores = new Object[valores.length * 2];
            for (int i = 0; i < cantidad; i++) {
                nuevasClaves[i] = claves[i];
                nuevosValores[i] = valores[i];
            }
            claves = nuevasClaves;
            valores = nuevosValores;
        }
        claves[cantidad] = clave;
        valores[cantidad] = valor;
        cantidad++;
    }

    /**
     * Busca un valor por su clave.
     *
     * @param clave nombre del campo
     * @return el valor, o null si la clave no existe
     */
    public Object get(String clave) {
        for (int i = 0; i < cantidad; i++) {
            if (claves[i].equals(clave)) {
                return valores[i];
            }
        }
        return null;
    }

    /**
     * @param clave nombre del campo
     * @return el campo como texto
     * @throws IllegalStateException si falta el campo o no es texto
     */
    public String getString(String clave) {
        Object v = get(clave);
        if (!(v instanceof String)) {
            throw new IllegalStateException("El campo '" + clave + "' no existe o no es texto.");
        }
        return (String) v;
    }

    /**
     * @param clave nombre del campo
     * @return el campo como entero
     * @throws IllegalStateException si falta el campo o no es número
     */
    public int getInt(String clave) {
        Object v = get(clave);
        if (!(v instanceof Integer)) {
            throw new IllegalStateException("El campo '" + clave + "' no existe o no es número.");
        }
        return (Integer) v;
    }

    /**
     * @param clave nombre del campo
     * @return el campo como objeto JSON
     * @throws IllegalStateException si falta el campo o no es un objeto
     */
    public JsonObjeto getObjeto(String clave) {
        Object v = get(clave);
        if (!(v instanceof JsonObjeto)) {
            throw new IllegalStateException("El campo '" + clave + "' no existe o no es un objeto.");
        }
        return (JsonObjeto) v;
    }

    /**
     * @param clave nombre del campo
     * @return el campo como arreglo
     * @throws IllegalStateException si falta el campo o no es un arreglo
     */
    public Object[] getArreglo(String clave) {
        Object v = get(clave);
        if (!(v instanceof Object[])) {
            throw new IllegalStateException("El campo '" + clave + "' no existe o no es un arreglo.");
        }
        return (Object[]) v;
    }
}