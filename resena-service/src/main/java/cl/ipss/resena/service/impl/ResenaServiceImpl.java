package cl.ipss.resena.service.impl;

import cl.ipss.resena.client.ProductoClient;
import cl.ipss.resena.dto.CrearResenaRequest;
import cl.ipss.resena.dto.ProductoRemoto;
import cl.ipss.resena.dto.ResenaResponse;
import cl.ipss.resena.entity.Resena;
import cl.ipss.resena.mapper.ResenaMapper;
import cl.ipss.resena.repository.ResenaRepository;
import cl.ipss.resena.service.ResenaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Lógica del negocio de reseñas. Inyección por constructor.
 *
 * No hay @Transactional: se guarda UN solo documento, y MongoDB ya lo escribe completo o no lo escribe.
 * Tampoco hay guardado automático como en JPA: lo que no se pasa a save() no se guarda.
 */
@Service
public class ResenaServiceImpl implements ResenaService {

    private static final Logger log = LoggerFactory.getLogger(ResenaServiceImpl.class);

    private final ResenaRepository repository;
    private final ProductoClient productoClient;
    private final ResenaMapper mapper;

    public ResenaServiceImpl(ResenaRepository repository, ProductoClient productoClient, ResenaMapper mapper) {
        this.repository = repository;
        this.productoClient = productoClient;
        this.mapper = mapper;
    }

    @Override
    public List<ResenaResponse> listar(Long productoId) {
        log.info("--- listing reviews: product={} ---", productoId);
        List<Resena> resenas = productoId == null
                ? repository.findAllByOrderByCreadoEnDesc()
                : repository.findByProductoIdOrderByCreadoEnDesc(productoId);
        return resenas.stream().map(mapper::aResponse).toList();
    }

    @Override
    public ResenaResponse crear(CrearResenaRequest request) {
        log.info("--- creating review: product={}, stars={} ---", request.productoId(), request.estrellas());

        // 1) el producto existe? (404 si no; 503 si producto-service está caído)
        ProductoRemoto producto = productoClient.obtenerProducto(request.productoId());

        // 2) el Builder valida y arma el documento
        Resena resena = Resena.builder()
                .productoId(producto.id())
                .nombreProducto(producto.nombre())
                .autor(request.autor())
                .estrellas(request.estrellas())
                .comentario(request.comentario())
                .build();

        // 3) se guarda en MongoDB (aquí se genera el id)
        return mapper.aResponse(repository.save(resena));
    }
}
