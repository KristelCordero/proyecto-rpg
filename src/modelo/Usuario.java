package modelo;

//Kristel Cordero Fernandez, C6E494
//Isaac Daniel Vega Leiton, C6N937
/**
 * Representa a un usuario del videojuego.
 * Agrupa sus datos generales y se relaciona por composición con
 * sus {@link EstadisticasJugador} y su {@link ConfiguracionJuego}.
 */
public class Usuario {

    private int id;
    private String nickname;
    private String password;
    private String nombrePersonaje;
    private EstadisticasJugador estadisticas;
    private ConfiguracionJuego configuracion;

    /**
     * Crea un usuario nuevo. Las estadísticas inician en sus valores iniciales
     * y la configuración con sus valores por defecto.
     *
     * @param id              identificador asignado por el sistema
     * @param nickname        entre 4 y 50 caracteres
     * @param password        entre 6 y 16 caracteres, con al menos una letra y un número
     * @param nombrePersonaje entre 2 y 30 caracteres
     * @throws IllegalArgumentException si algún dato incumple las reglas
     */
    public Usuario(int id, String nickname, String password, String nombrePersonaje) {
        this(id, nickname, password, nombrePersonaje,
                new EstadisticasJugador(), new ConfiguracionJuego());
    }

    /**
     * Crea un usuario con estadísticas y configuración existentes (al cargar el JSON).
     *
     * @param id              identificador del usuario
     * @param nickname        entre 4 y 50 caracteres
     * @param password        entre 6 y 16 caracteres, con al menos una letra y un número
     * @param nombrePersonaje entre 2 y 30 caracteres
     * @param estadisticas    estadísticas del jugador
     * @param configuracion   configuración del juego
     * @throws IllegalArgumentException si algún dato incumple las reglas
     */
    public Usuario(int id, String nickname, String password, String nombrePersonaje,
                   EstadisticasJugador estadisticas, ConfiguracionJuego configuracion) {
        this.id = id;
        setNickname(nickname);
        setPassword(password);
        setNombrePersonaje(nombrePersonaje);
        this.estadisticas = estadisticas;
        this.configuracion = configuracion;
    }

    /** @return identificador único (no tiene setter: nunca cambia) */
    public int getId() { return id; }

    /** @return nickname */
    public String getNickname() { 
    	return nickname; }

    /** @return contraseña */
    public String getPassword() {
    	return password; }

    /** @return nombre del personaje */
    public String getNombrePersonaje() { 
    	return nombrePersonaje; }

    /** @return estadísticas del jugador */
    public EstadisticasJugador getEstadisticas() { 
    	return estadisticas; }

    /** @return configuración del juego */
    public ConfiguracionJuego getConfiguracion() { 
    	return configuracion; }

    /**
     * Asigna el nickname.que sea único no se revisa aquí,eso lo revisa el DAO.
     *
     * @param nickname entre 4 y 50 caracteres
     * @throws IllegalArgumentException si la longitud es inválida
     */
    private void setNickname(String nickname) {
        if (nickname == null || nickname.length() < 4 || nickname.length() > 50) {
            throw new IllegalArgumentException("El nickname debe tener entre 4 y 50 caracteres.");
        }
        this.nickname = nickname;
    }

    /**
     * Cambia la contraseña aplicando las mismas reglas del registro.
     *
     * @param password entre 6 y 16 caracteres, con al menos una letra y un número
     * @throws IllegalArgumentException si no cumple eso
     */
    public void setPassword(String password) {
        if (password == null || password.length() < 6 || password.length() > 16) {
            throw new IllegalArgumentException("La contraseña debe tener entre 6 y 16 caracteres.");
        }
        boolean hayLetra = false;
        boolean hayNumero = false;
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isLetter(c)) {
                hayLetra = true;
            }
            if (Character.isDigit(c)) {
                hayNumero = true;
            }
        }
        if (!hayLetra || !hayNumero) {
            throw new IllegalArgumentException("La contraseña debe incluir al menos una letra y un número.");
        }
        this.password = password;
    }

    /**
     * Cambia el nombre del personaje.
     *
     * @param nombrePersonaje entre 2 y 30 caracteres
     * @throws IllegalArgumentException si la longitud es inválida
     */
    public void setNombrePersonaje(String nombrePersonaje) {
        if (nombrePersonaje == null || nombrePersonaje.length() < 2 || nombrePersonaje.length() > 30) {
            throw new IllegalArgumentException("El nombre del personaje debe tener entre 2 y 30 caracteres.");
        }
        this.nombrePersonaje = nombrePersonaje;
    }
}