package cl.ipss.resena.dto;

import java.time.LocalDateTime;

/** Lo que la API responde. El id es texto porque lo genera MongoDB. */
public record ResenaResponse(String id, Long productoId, String nombreProducto, String autor,
                             Integer estrellas, String comentario, LocalDateTime creadoEn) {
}
