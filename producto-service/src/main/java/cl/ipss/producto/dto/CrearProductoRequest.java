package cl.ipss.producto.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/** Lo que Angular envía para crear un producto. Un record es inmutable y no necesita getters. */
public record CrearProductoRequest(
        @NotBlank @Size(max = 100) String nombre,
        @NotNull @DecimalMin("0.01") BigDecimal precio,
        @NotNull @Min(0) Integer stock) {
}
