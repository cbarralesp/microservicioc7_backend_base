package cl.ipss.resena.dto;

/**
 * Lo que este servicio necesita de la respuesta de producto-service (el resto se ignora).
 * Es un DTO de ESTE servicio: no se comparte código entre microservicios.
 */
public record ProductoRemoto(Long id, String nombre) {
}
