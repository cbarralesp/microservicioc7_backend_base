package cl.ipss.resena.client;

import cl.ipss.resena.dto.ProductoRemoto;

/**
 * Puerto de salida hacia producto-service.
 * El service depende de esta INTERFAZ, no de HTTP: en los tests se reemplaza por un mock.
 */
public interface ProductoClient {

    /** Datos del producto. Lanza RecursoNoEncontradoException si no existe. */
    ProductoRemoto obtenerProducto(Long productoId);
}
