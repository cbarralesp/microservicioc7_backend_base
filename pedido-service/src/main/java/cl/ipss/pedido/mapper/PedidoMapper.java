package cl.ipss.pedido.mapper;

import cl.ipss.pedido.dto.PedidoResponse;
import cl.ipss.pedido.entity.Pedido;
import org.springframework.stereotype.Component;

/** Convierte entity -> DTO a mano. */
@Component
public class PedidoMapper {

    public PedidoResponse aResponse(Pedido p) {
        return new PedidoResponse(p.getId(), p.getProductoId(), p.getNombreProducto(), p.getPrecioUnitario(),
                p.getCantidad(), p.getTotal(), p.getCreadoEn());
    }
}
