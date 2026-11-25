package cl.ipss.producto.dto;

import java.math.BigDecimal;

/** Lo que la API devuelve: nunca se expone la entity directamente. */
public record ProductoResponse(Long id, String nombre, BigDecimal precio, Integer stock) {
}
