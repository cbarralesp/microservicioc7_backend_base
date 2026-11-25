package cl.ipss.pedido.exception;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Formato único de error que recibe Angular. También se construye con el PATRÓN BUILDER:
 * ApiError.builder().status(404).error("Not Found").mensaje("...").build()
 */
public record ApiError(LocalDateTime fecha, int status, String error, String mensaje, List<String> detalles) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private int status;
        private String error;
        private String mensaje;
        private List<String> detalles = List.of();

        private Builder() {
        }

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder error(String error) {
            this.error = error;
            return this;
        }

        public Builder mensaje(String mensaje) {
            this.mensaje = mensaje;
            return this;
        }

        public Builder detalles(List<String> detalles) {
            this.detalles = detalles;
            return this;
        }

        public ApiError build() {
            return new ApiError(LocalDateTime.now(), status, error, mensaje, detalles);
        }
    }
}
