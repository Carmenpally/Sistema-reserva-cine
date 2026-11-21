package excepciones;

/**
 * Error de negocio: datos incorrectos o una regla del cine que se violó.
 * (Los errores de lectura y escritura de archivos van en otra excepción.)
 */
public class ReglaNegocioException extends Exception {

    private static final long serialVersionUID = 1L;

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}