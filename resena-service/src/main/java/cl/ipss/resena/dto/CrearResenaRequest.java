package cl.ipss.resena.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Lo que el cliente envía para dejar una reseña. */
public record CrearResenaRequest(@NotNull Long productoId,
                                 @NotBlank @Size(max = 60) String autor,
                                 @NotNull @Min(1) @Max(5) Integer estrellas,
                                 @NotBlank @Size(max = 500) String comentario) {
}
