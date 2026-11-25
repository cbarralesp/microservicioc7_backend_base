package cl.ipss.producto.service.impl;

import cl.ipss.producto.dto.CrearProductoRequest;
import cl.ipss.producto.dto.ProductoResponse;
import cl.ipss.producto.entity.Producto;
import cl.ipss.producto.exception.RecursoNoEncontradoException;
import cl.ipss.producto.mapper.ProductoMapper;
import cl.ipss.producto.repository.ProductoRepository;
import cl.ipss.producto.service.ProductoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Lógica del negocio. Inyección por constructor (sin @Autowired en campos). */
@Service
public class ProductoServiceImpl implements ProductoService {

    private static final Logger log = LoggerFactory.getLogger(ProductoServiceImpl.class);

    private final ProductoRepository repository;
    private final ProductoMapper mapper;

    public ProductoServiceImpl(ProductoRepository repository, ProductoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> listar() {
        log.info("--- listing products ---");
        return repository.findAll().stream().map(mapper::aResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponse obtener(Long id) {
        return mapper.aResponse(buscarOFallar(id));
    }

    @Override
    @Transactional
    public ProductoResponse crear(CrearProductoRequest request) {
        log.info("--- creating product: {} ---", request.nombre());
        Producto guardado = repository.save(mapper.aEntity(request));
        return mapper.aResponse(guardado);
    }

    @Override
    @Transactional
    public ProductoResponse descontarStock(Long id, int cantidad) {
        log.info("--- discounting stock: product={}, quantity={} ---", id, cantidad);
        Producto producto = buscarOFallar(id);
        producto.descontarStock(cantidad);   // la regla vive en la entity
        return mapper.aResponse(producto);   // @Transactional guarda el cambio al terminar
    }

    private Producto buscarOFallar(Long id) {
        return repository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException(id));
    }
}
