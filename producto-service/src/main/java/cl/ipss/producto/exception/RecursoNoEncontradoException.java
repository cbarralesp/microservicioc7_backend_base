package cl.ipss.producto.exception;

/** El producto buscado no existe. Se traduce a HTTP 404. */
public class RecursoNoEncontradoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RecursoNoEncontradoException(Long id) {
        super("No existe un producto con id " + id);
    }
}
