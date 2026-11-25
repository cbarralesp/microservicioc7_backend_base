package cl.ipss.resena.exception;

/** Otro microservicio no responde (caído o demasiado lento). Se traduce a HTTP 503. */
public class ServicioNoDisponibleException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ServicioNoDisponibleException(String servicio) {
        super("El servicio '" + servicio + "' no está disponible en este momento. Intenta más tarde");
    }
}
