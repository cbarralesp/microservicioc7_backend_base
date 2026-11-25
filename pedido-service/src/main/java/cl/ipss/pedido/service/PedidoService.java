package cl.ipss.pedido.service;

import cl.ipss.pedido.dto.CrearPedidoRequest;
import cl.ipss.pedido.dto.PedidoResponse;

import java.util.List;

/** Contrato del negocio de pedidos. */
public interface PedidoService {

    List<PedidoResponse> listar();

    /** Crea un pedido: valida el producto, descuenta stock en producto-service y guarda. */
    PedidoResponse crear(CrearPedidoRequest request);
}
