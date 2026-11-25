package cl.ipss.pedido.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Lo que la API devuelve. */
public record PedidoResponse(Long id, Long productoId, String nombreProducto, BigDecimal precioUnitario,
                             Integer cantidad, BigDecimal total, LocalDateTime creadoEn) {
}
