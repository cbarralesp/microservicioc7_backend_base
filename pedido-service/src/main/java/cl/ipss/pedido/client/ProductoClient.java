package cl.ipss.pedido.client;

import cl.ipss.pedido.dto.ProductoRemoto;

/**
 * Puerto de salida hacia producto-service.
 * El service de pedidos depende de esta INTERFAZ, no de HTTP: en los tests se reemplaza por un mock.
 */
public interface ProductoClient {

    /** Datos del producto. Lanza RecursoNoEncontradoException si no existe. */
    ProductoRemoto obtenerProducto(Long productoId);

    /** Descuenta stock. Lanza StockInsuficienteException si no alcanza. */
    void descontarStock(Long productoId, int cantidad);
}
