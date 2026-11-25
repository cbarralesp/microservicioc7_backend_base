package cl.ipss.producto.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/** Lo que pedido-service envía para descontar stock. */
public record DescontarStockRequest(@NotNull @Min(1) Integer cantidad) {
}
