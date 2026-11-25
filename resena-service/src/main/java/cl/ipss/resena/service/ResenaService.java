package cl.ipss.resena.service;

import cl.ipss.resena.dto.CrearResenaRequest;
import cl.ipss.resena.dto.ResenaResponse;

import java.util.List;

/** Casos de uso de las reseñas. */
public interface ResenaService {

    /** De la más reciente a la más antigua. Con productoId null devuelve todas. */
    List<ResenaResponse> listar(Long productoId);

    ResenaResponse crear(CrearResenaRequest request);
}
