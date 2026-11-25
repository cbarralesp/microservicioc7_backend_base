package cl.ipss.pedido.dto;

import java.math.BigDecimal;

/** Lo que responde producto-service. Es un DTO de ESTE servicio: no se comparte código entre microservicios. */
public record ProductoRemoto(Long id, String nombre, BigDecimal precio, Integer stock) {
}
