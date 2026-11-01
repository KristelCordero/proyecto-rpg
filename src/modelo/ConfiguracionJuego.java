package modelo;

/**
 * Configuración personalizada del videojuego para un usuario.
 * Cada valor se valida contra un rango o una lista de opciones si el valor
 * no es válido se lanza una {@link IllegalArgumentException} con un mensaje claro.
 */
public class ConfiguracionJuego {

    private static final String[] DIFICULTADES = {"Fácil", "Normal", "Difícil"};
    private static final String[] VELOCIDADES = {"Lenta", "Normal", "Rápida"};
    private static final int CORAZONES_MIN = 1;
    private static final int CORAZONES_MAX = 10;
    private static final int ENEMIGOS_MIN = 1;
    private static final int ENEMIGOS_MAX = 15;
    private static final int VOLUMEN_MIN = 0;
    private static final int VOLUMEN_MAX = 100;

    private String dificultad;
    private int corazones;
    private String velocidad;
    private int enemigosPorZona;
    private int volumen;

    /** Crea una configuración con valores por defecto válidos. */
    public ConfiguracionJuego() {
        this.dificultad = "Normal";
        this.corazones = 3;
        this.velocidad = "Normal";
        this.enemigosPorZona = 5;
        this.volumen = 70;
    }

    /**
     * Crea una configuración validando cada valor.
     *
     * @param dificultad      una de las opciones de {@link #getOpcionesDificultad()}
     * @param corazones       entre 1 y 10
     * @param velocidad       una de las opciones de {@link #getOpcionesVelocidad()}
     * @param enemigosPorZona entre 1 y 15
     * @param volumen         entre 0 y 100
     * @throws IllegalArgumentException si algún valor es inválido
     */
    public ConfiguracionJuego(String dificultad, int corazones, String velocidad,
                              int enemigosPorZona, int volumen) {
        setDificultad(dificultad);
        setCorazones(corazones);
        setVelocidad(velocidad);
        setEnemigosPorZona(enemigosPorZona);
        setVolumen(volumen);
    }

    /** @return dificultad inicial */
    public String getDificultad() {
    	return dificultad; }

    /** @return corazones iniciales */
    public int getCorazones() { 
    	return corazones; }

    /** @return velocidad del personaje */
    public String getVelocidad() {
    	return velocidad; }

    /** @return enemigos por zona */
    public int getEnemigosPorZona() { 
    	return enemigosPorZona; }

    /** @return volumen de sonido (0 a 100) */
    public int getVolumen() { 
    	return volumen; }

    /**
     * @param dificultad debe estar en la lista de dificultades
     * @throws IllegalArgumentException si no es una opción válida
     */
    public void setDificultad(String dificultad) {
        if (!estaEn(DIFICULTADES, dificultad)) {
            throw new IllegalArgumentException("La dificultad debe ser: Fácil, Normal o Difícil.");
        }
        this.dificultad = dificultad;
    }

    /**
     * @param corazones debe estar entre 1 y 10
     * @throws IllegalArgumentException si está fuera de rango
     */
    public void setCorazones(int corazones) {
        if (corazones < CORAZONES_MIN || corazones > CORAZONES_MAX) {
            throw new IllegalArgumentException("Los corazones deben estar entre "
                    + CORAZONES_MIN + " y " + CORAZONES_MAX + ".");
        }
        this.corazones = corazones;
    }

    /**
     * @param velocidad debe estar en la lista de velocidades
     * @throws IllegalArgumentException si no es una opción válida
     */
    public void setVelocidad(String velocidad) {
        if (!estaEn(VELOCIDADES, velocidad)) {
            throw new IllegalArgumentException("La velocidad debe ser: Lenta, Normal o Rápida.");
        }
        this.velocidad = velocidad;
    }

    /**
     * @param enemigosPorZona debe estar entre 1 y 15
     * @throws IllegalArgumentException si está fuera de rango
     */
    public void setEnemigosPorZona(int enemigosPorZona) {
        if (enemigosPorZona < ENEMIGOS_MIN || enemigosPorZona > ENEMIGOS_MAX) {
            throw new IllegalArgumentException("Los enemigos por zona deben estar entre "
                    + ENEMIGOS_MIN + " y " + ENEMIGOS_MAX + ".");
        }
        this.enemigosPorZona = enemigosPorZona;
    }

    /**
     * @param volumen debe estar entre 0 y 100
     * @throws IllegalArgumentException si está fuera de rango
     */
    public void setVolumen(int volumen) {
        if (volumen < VOLUMEN_MIN || volumen > VOLUMEN_MAX) {
            throw new IllegalArgumentException("El volumen debe estar entre "
                    + VOLUMEN_MIN + " y " + VOLUMEN_MAX + ".");
        }
        this.volumen = volumen;
    }

    /** @return copia de las dificultades permitidas (para llenar un combo en la ventana) */
    public String[] getOpcionesDificultad() { 
    	return DIFICULTADES.clone(); }

    /** @return copia de las velocidades permitidas */
    public String[] getOpcionesVelocidad() { return
    		VELOCIDADES.clone(); }

    /** @return corazones mínimos permitidos */
    public int getCorazonesMin() { 
    	return CORAZONES_MIN; }

    /** @return corazones máximos permitidos */
    public int getCorazonesMax() {
    	return CORAZONES_MAX; }

    /** @return mínimo de enemigos por zona */
    public int getEnemigosMin() { 
    	return ENEMIGOS_MIN; }

    /** @return máximo de enemigos por zona */
    public int getEnemigosMax() { 
    	return ENEMIGOS_MAX; }

    /** Recorre el arreglo buscando el valor */
    private boolean estaEn(String[] opciones, String valor) {
        for (int i = 0; i < opciones.length; i++) {
            if (opciones[i].equals(valor)) {
                return true;
            }
        }
        return false;
    }
}