package cl.ipss.resena.exception;

/** El recurso buscado no existe. Se traduce a HTTP 404. */
public class RecursoNoEncontradoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RecursoNoEncontradoException(String recurso, Long id) {
        super("No existe un " + recurso + " con id " + id);
    }
}
