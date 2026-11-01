package modelo;
//Kristel Cordero Fernandez, C6E494
/**
 * Estadísticas de un jugador
 * el programa de mantenimiento solo las consulta, nunca las edita
 */
public class EstadisticasJugador {

    private int nivelMaximo;
    private int puntajeMaximo;
    private int partidasJugadas;
    private int enemigosDerrotados;

    /** Crea estadísticas iniciales de un usuario nuevo: nivel 1 y todo lo demás en 0. */
    public EstadisticasJugador() {
        this.nivelMaximo = 1;
        this.puntajeMaximo = 0;
        this.partidasJugadas = 0;
        this.enemigosDerrotados = 0;
    }

    /**
     * Crea estadísticas con valores ya existentes por ejemplo, leídos desde el JSON
     *
     * @param nivelMaximo        nivel más alto alcanzado
     * @param puntajeMaximo      mejor puntaje
     * @param partidasJugadas    cantidad de partidas
     * @param enemigosDerrotados cantidad de enemigos vencidos
     */
    public EstadisticasJugador(int nivelMaximo, int puntajeMaximo,
                               int partidasJugadas, int enemigosDerrotados) {
        this.nivelMaximo = nivelMaximo;
        this.puntajeMaximo = puntajeMaximo;
        this.partidasJugadas = partidasJugadas;
        this.enemigosDerrotados = enemigosDerrotados;
    }

    /** @return nivel máximo alcanzado */
    public int getNivelMaximo() {
    	return nivelMaximo; }

    /** @return puntaje máximo */
    public int getPuntajeMaximo() {
    	return puntajeMaximo; }

    /** @return partidas jugadas */
    public int getPartidasJugadas() {
    	return partidasJugadas; }

    /** @return enemigos derrotados */
    public int getEnemigosDerrotados() { 
    	return enemigosDerrotados; }

    // Los métodos siguientes los usará el videojuego, no la ventana de mantenimiento.

    /** Suma una partida jugada. */
    public void registrarPartida() {
    	partidasJugadas++; }

    /** Suma un enemigo derrotado. */
    public void registrarEnemigoDerrotado() { 
    	enemigosDerrotados++; }

    /**
     * Actualiza el puntaje máximo solo si el nuevo puntaje lo supera.
     *
     * @param puntaje puntaje obtenido en la partida
     */
    public void registrarPuntaje(int puntaje) {
        if (puntaje > puntajeMaximo) {
            puntajeMaximo = puntaje;
        }
    }

    /**
     * Actualiza el nivel máximo si el nuevo nivel lo supera.
     *
     * @param nivel nivel alcanzado en la partida
     */
    public void registrarNivel(int nivel) {
        if (nivel > nivelMaximo) {
            nivelMaximo = nivel;
        }
    }
}