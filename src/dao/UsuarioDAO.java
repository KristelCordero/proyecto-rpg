package dao;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import json.JsonLector;
import json.JsonObjeto;
import modelo.ConfiguracionJuego;
import modelo.EstadisticasJugador;
import modelo.Usuario;

/**
 * DAO de usuarios. Es la  clase que sabe cómo y dónde
 * se guardan los datos mantiene el dataset en un arreglo nativo y lo sincroniza
 * con un archivo JSON
 */
public class UsuarioDAO {

    private static final int CAPACIDAD_INICIAL = 10;

    private final Path archivo;
    private Usuario[] usuarios;
    private int cantidad;      // cuántas posiciones del arreglo están realmente en uso
    private int siguienteId;   // contador histórico

    /**
     * Crea el DAO y carga los datos del archiv.
     *
     * @param rutaArchivo ruta del archivo JSON, por ejemplo "datos/usuarios.json"
     * @throws IllegalStateException si el archivo existe pero no se puede leer
     */
    public UsuarioDAO(String rutaArchivo) {
        this.archivo = Paths.get(rutaArchivo);
        this.usuarios = new Usuario[CAPACIDAD_INICIAL];
        this.cantidad = 0;
        this.siguienteId = 1;
        cargar();
    }

    // ------------------------------------------------------------ CREATE

    /**
     * Registra un usuario nuevo y guarda el archivo
     *
     * @param nickname        entre 4 y 50 caracteres, unico
     * @param password        entre 6 y 16 caracteres, con letra y número
     * @param nombrePersonaje entre 2 y 30 caracteres
     * @return el usuario creado, ya con su id
     * @throws IllegalArgumentException si algún dato es inválido o el nickname ya existe
     */
    public Usuario insertar(String nickname, String password, String nombrePersonaje) {
        Usuario nuevo = new Usuario(siguienteId, nickname, password, nombrePersonaje);
        
        if (buscarPorNickname(nickname) != null) {
            throw new IllegalArgumentException("El nickname '" + nickname + "' ya está en uso.");
        }
        
        siguienteId++;
        agregar(nuevo);
        guardar();
        return nuevo;
    }

    // ------------------------------------------------------------ READ

    /** @return copia del arreglo con todos los usuarios*/
    public Usuario[] obtenerTodos() {
        Usuario[] copia = new Usuario[cantidad];
        for (int i = 0; i < cantidad; i++) {
            copia[i] = usuarios[i];
        }
        return copia;
    }

    /**
     * @param id identificador buscado
     * @return el usuario o null si no existe
     */
    public Usuario buscarPorId(int id) {
        int i = posicionDeId(id);
        return (i == -1) ? null : usuarios[i];
    }

    /**
     * Búsqueda exacta para validar el nickname único.
     *
     * @param nickname nickname buscado
     * @return el usuario o null si no existe
     */
    public Usuario buscarPorNickname(String nickname) {
        for (int i = 0; i < cantidad; i++) {
            if (usuarios[i].getNickname().equalsIgnoreCase(nickname)) {
                return usuarios[i];
            }
        }
        return null;
    }

    /**
     * Búsqueda parcial para la pantalla de consulta ej: "Her" encuentra "Heroe123"
     *
     * @param texto fragmento del nickname
     * @return arreglo de tamaño exacto con las coincidencias 
     */
    public Usuario[] buscarPorNicknameParcial(String texto) {
        String buscado = texto.toLowerCase();
        Usuario[] encontrados = new Usuario[cantidad];
        int n = 0;
        for (int i = 0; i < cantidad; i++) {
            if (usuarios[i].getNickname().toLowerCase().contains(buscado)) {
                encontrados[n++] = usuarios[i];
            }
        }
        return recortar(encontrados, n);
    }

    /**
     * @param nivel nivel máximo alcanzado
     * @return todos los usuarios con ese nivel máximo
     */
    public Usuario[] buscarPorNivelMaximo(int nivel) {
        Usuario[] encontrados = new Usuario[cantidad];
        int n = 0;
        for (int i = 0; i < cantidad; i++) {
            if (usuarios[i].getEstadisticas().getNivelMaximo() == nivel) {
                encontrados[n++] = usuarios[i];
            }
        }
        return recortar(encontrados, n);
    }

    /**
     * @param puntaje puntaje máximo
     * @return todos los usuarios con ese puntaje máximo
     */
    public Usuario[] buscarPorPuntajeMaximo(int puntaje) {
        Usuario[] encontrados = new Usuario[cantidad];
        int n = 0;
        for (int i = 0; i < cantidad; i++) {
            if (usuarios[i].getEstadisticas().getPuntajeMaximo() == puntaje) {
                encontrados[n++] = usuarios[i];
            }
        }
        return recortar(encontrados, n);
    }

    //  UPDATE

    /**
     * Guarda en el archivo los cambios hechos a un usuario. Como el arreglo guarda
     * referencias a los objetos, al llamar a sus setters ya quedan modificados
     * en memoria; solo falta escribirlos al JSON.
     */
    public void guardarCambios() {
        guardar();
    }

    // DELETE

    /**
     * Elimina un usuario. Su id NO se reutiliza: siguienteId no se toca.
     *
     * @param id identificador del usuario a eliminar
     * @return true si existía y se eliminó, false si no se encontró
     */
    public boolean eliminar(int id) {
        int pos = posicionDeId(id);
        if (pos == -1) {
            return false;
        }
        // Se corren todos los elementos siguientes una posición a la izquierda.
        for (int i = pos; i < cantidad - 1; i++) {
            usuarios[i] = usuarios[i + 1];
        }
        usuarios[cantidad - 1] = null;   // se libera la última posición
        cantidad--;
        guardar();
        return true;
    }

    // ------------------------------------------------------------ ARREGLOS (privado)

    private void agregar(Usuario u) {
        if (cantidad == usuarios.length) {
            Usuario[] masGrande = new Usuario[usuarios.length * 2];
            for (int i = 0; i < cantidad; i++) {
                masGrande[i] = usuarios[i];
            }
            usuarios = masGrande;
        }
        usuarios[cantidad] = u;
        cantidad++;
    }

    private int posicionDeId(int id) {
        for (int i = 0; i < cantidad; i++) {
            if (usuarios[i].getId() == id) {
                return i;
            }
        }
        return -1;
    }

    private Usuario[] recortar(Usuario[] origen, int n) {
        Usuario[] resultado = new Usuario[n];
        for (int i = 0; i < n; i++) {
            resultado[i] = origen[i];
        }
        return resultado;
    }

    // JSON (privado)

    private void cargar() {
        if (!Files.exists(archivo)) {
            return;   // primera ejecución, todavía no hay datos
        }
        try {
            String contenido = new String(Files.readAllBytes(archivo), StandardCharsets.UTF_8);
            JsonObjeto raiz = (JsonObjeto) new JsonLector(contenido).leer();
            siguienteId = raiz.getInt("siguienteId");
            Object[] lista = raiz.getArreglo("usuarios");
            for (int i = 0; i < lista.length; i++) {
                agregar(aUsuario((JsonObjeto) lista[i]));
            }
        } catch (IOException | RuntimeException e) {
            throw new IllegalStateException("No se pudo leer " + archivo + ": " + e.getMessage(), e);
        }
    }

    private Usuario aUsuario(JsonObjeto o) {
        JsonObjeto e = o.getObjeto("estadisticas");
        JsonObjeto c = o.getObjeto("configuracion");
        EstadisticasJugador est = new EstadisticasJugador(
                e.getInt("nivelMaximo"), e.getInt("puntajeMaximo"),
                e.getInt("partidasJugadas"), e.getInt("enemigosDerrotados"));
        ConfiguracionJuego conf = new ConfiguracionJuego(
                c.getString("dificultad"), c.getInt("corazones"), c.getString("velocidad"),
                c.getInt("enemigosPorZona"), c.getInt("volumen"));
        return new Usuario(o.getInt("id"), o.getString("nickname"), o.getString("password"),
                o.getString("personaje"), est, conf);
    }

    private void guardar() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n  \"siguienteId\": ").append(siguienteId).append(",\n  \"usuarios\": [\n");
        for (int i = 0; i < cantidad; i++) {
            Usuario u = usuarios[i];
            EstadisticasJugador e = u.getEstadisticas();
            ConfiguracionJuego c = u.getConfiguracion();
            sb.append("    {\n");
            sb.append("      \"id\": ").append(u.getId()).append(",\n");
            sb.append("      \"nickname\": ").append(texto(u.getNickname())).append(",\n");
            sb.append("      \"password\": ").append(texto(u.getPassword())).append(",\n");
            sb.append("      \"personaje\": ").append(texto(u.getNombrePersonaje())).append(",\n");
            sb.append("      \"estadisticas\": {\"nivelMaximo\": ").append(e.getNivelMaximo())
              .append(", \"puntajeMaximo\": ").append(e.getPuntajeMaximo())
              .append(", \"partidasJugadas\": ").append(e.getPartidasJugadas())
              .append(", \"enemigosDerrotados\": ").append(e.getEnemigosDerrotados()).append("},\n");
            sb.append("      \"configuracion\": {\"dificultad\": ").append(texto(c.getDificultad()))
              .append(", \"corazones\": ").append(c.getCorazones())
              .append(", \"velocidad\": ").append(texto(c.getVelocidad()))
              .append(", \"enemigosPorZona\": ").append(c.getEnemigosPorZona())
              .append(", \"volumen\": ").append(c.getVolumen()).append("}\n");
            sb.append("    }").append(i < cantidad - 1 ? "," : "").append("\n");
        }
        sb.append("  ]\n}\n");
        try {
            Path carpeta = archivo.toAbsolutePath().getParent();
            if (carpeta != null) {
                Files.createDirectories(carpeta);
            }
            Files.write(archivo, sb.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException ex) {
            throw new IllegalStateException("No se pudo guardar " + archivo + ": " + ex.getMessage(), ex);
        }
    }

    /** Convierte un texto Java en un texto JSON entre comillas, escapando lo necesario */
    private String texto(String s) {
        StringBuilder sb = new StringBuilder("\"");
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '"' || c == '\\') {
                sb.append('\\').append(c);
            } else if (c == '\n') {
                sb.append("\\n");
            } else if (c == '\t') {
                sb.append("\\t");
            } else {
                sb.append(c);
            }
        }
        return sb.append('"').toString();
    }
}