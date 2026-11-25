package cl.ipss.pedido.exception;

/** El producto no tiene stock suficiente (lo decide producto-service). Se traduce a HTTP 409. */
public class StockInsuficienteException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public StockInsuficienteException(Long productoId) {
        super("No hay stock suficiente del producto " + productoId + " para completar el pedido");
    }
}
