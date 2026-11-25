package cl.ipss.resena.mapper;

import cl.ipss.resena.dto.ResenaResponse;
import cl.ipss.resena.entity.Resena;
import org.springframework.stereotype.Component;

/** Convierte documento -> DTO a mano. */
@Component
public class ResenaMapper {

    public ResenaResponse aResponse(Resena r) {
        return new ResenaResponse(r.getId(), r.getProductoId(), r.getNombreProducto(), r.getAutor(),
                r.getEstrellas(), r.getComentario(), r.getCreadoEn());
    }
}
