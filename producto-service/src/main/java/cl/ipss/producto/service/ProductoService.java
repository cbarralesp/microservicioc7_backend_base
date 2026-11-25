package cl.ipss.producto.service;

import cl.ipss.producto.dto.CrearProductoRequest;
import cl.ipss.producto.dto.ProductoResponse;

import java.util.List;

/** Contrato del negocio: el controller depende de esta interfaz, no de la implementación. */
public interface ProductoService {

    List<ProductoResponse> listar();

    ProductoResponse obtener(Long id);

    ProductoResponse crear(CrearProductoRequest request);

    /** Descuenta unidades. Lanza StockInsuficienteException si no alcanzan. */
    ProductoResponse descontarStock(Long id, int cantidad);
}
