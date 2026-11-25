package cl.ipss.producto.exception;

/** No hay unidades suficientes para descontar. Se traduce a HTTP 409. */
public class StockInsuficienteException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public StockInsuficienteException(String producto, int disponible) {
        super("Stock insuficiente de '" + producto + "': quedan " + disponible + " unidades");
    }
}
