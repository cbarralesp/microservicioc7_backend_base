package cl.ipss.producto.mapper;

import cl.ipss.producto.dto.CrearProductoRequest;
import cl.ipss.producto.dto.ProductoResponse;
import cl.ipss.producto.entity.Producto;
import org.springframework.stereotype.Component;

/** Convierte entity <-> DTO a mano (sin librerías): así se ve exactamente qué pasa. */
@Component
public class ProductoMapper {

    public Producto aEntity(CrearProductoRequest request) {
        return Producto.builder()
                .nombre(request.nombre())
                .precio(request.precio())
                .stock(request.stock())
                .build();
    }

    public ProductoResponse aResponse(Producto producto) {
        return new ProductoResponse(producto.getId(), producto.getNombre(), producto.getPrecio(),
                producto.getStock());
    }
}
