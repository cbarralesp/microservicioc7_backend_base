package cl.ipss.pedido.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/** Lo que Angular envía para hacer un pedido. */
public record CrearPedidoRequest(@NotNull Long productoId, @NotNull @Min(1) Integer cantidad) {
}
