package cl.ipss.pedido.service.impl;

import cl.ipss.pedido.client.ProductoClient;
import cl.ipss.pedido.dto.CrearPedidoRequest;
import cl.ipss.pedido.dto.PedidoResponse;
import cl.ipss.pedido.dto.ProductoRemoto;
import cl.ipss.pedido.entity.Pedido;
import cl.ipss.pedido.exception.StockInsuficienteException;
import cl.ipss.pedido.mapper.PedidoMapper;
import cl.ipss.pedido.repository.PedidoRepository;
import cl.ipss.pedido.service.PedidoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Lógica del negocio de pedidos. Inyección por constructor. */
@Service
public class PedidoServiceImpl implements PedidoService {

    private static final Logger log = LoggerFactory.getLogger(PedidoServiceImpl.class);

    private final PedidoRepository repository;
    private final ProductoClient productoClient;
    private final PedidoMapper mapper;

    public PedidoServiceImpl(PedidoRepository repository, ProductoClient productoClient, PedidoMapper mapper) {
        this.repository = repository;
        this.productoClient = productoClient;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponse> listar() {
        log.info("--- listing orders ---");
        return repository.findAllByOrderByCreadoEnDesc().stream().map(mapper::aResponse).toList();
    }

    @Override
    @Transactional
    public PedidoResponse crear(CrearPedidoRequest request) {
        log.info("--- creating order: product={}, quantity={} ---", request.productoId(), request.cantidad());

        // 1) el producto existe? (404 si no; 503 si producto-service está caído)
        ProductoRemoto producto = productoClient.obtenerProducto(request.productoId());

        // TODO 3.1: si producto.stock() < request.cantidad() -> lanzar StockInsuficienteException(producto.id())
        if (producto.stock() < request.cantidad()) {
        	throw new StockInsuficienteException(producto.id());
        }
        // TODO 3.2: descontar el stock en producto-service con productoClient.descontarStock(...)
        	productoClient.descontarStock(producto.id(), request.cantidad());
        // TODO 3.3: construir el Pedido con Pedido.builder() (productoId, nombreProducto, precioUnitario, cantidad)
        // TODO 3.4: guardar con repository.save(...) y devolver mapper.aResponse(...)
       Pedido pedido = Pedido.builder()
    		   .productoId(producto.id())
    		   .nombreProducto(producto.nombre())
    		   .precioUnitario(producto.precio())
    		   .cantidad(request.cantidad())
    		   .build();
       	return mapper.aResponse(repository.save(pedido));
       
    }
}
