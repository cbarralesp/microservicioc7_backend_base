package cl.ipss.pedido.service;

import cl.ipss.pedido.client.ProductoClient;
import cl.ipss.pedido.dto.CrearPedidoRequest;
import cl.ipss.pedido.dto.PedidoResponse;
import cl.ipss.pedido.dto.ProductoRemoto;
import cl.ipss.pedido.entity.Pedido;
import cl.ipss.pedido.exception.RecursoNoEncontradoException;
import cl.ipss.pedido.exception.ServicioNoDisponibleException;
import cl.ipss.pedido.exception.StockInsuficienteException;
import cl.ipss.pedido.mapper.PedidoMapper;
import cl.ipss.pedido.repository.PedidoRepository;
import cl.ipss.pedido.service.impl.PedidoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

/** producto-service y la base se simulan: se prueba solo la lógica del pedido. */
@ExtendWith(MockitoExtension.class)
class PedidoServiceImplTest {

    @Mock
    private PedidoRepository repository;

    @Mock
    private ProductoClient productoClient;

    private PedidoService service() {
        return new PedidoServiceImpl(repository, productoClient, new PedidoMapper());
    }

    private ProductoRemoto producto(int stock) {
        return new ProductoRemoto(1L, "Teclado", new BigDecimal("1000"), stock);
    }

    @Test
    void creaElPedidoDescuentaStockYCalculaElTotal() {
        when(productoClient.obtenerProducto(1L)).thenReturn(producto(10));
        when(repository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

        PedidoResponse r = service().crear(new CrearPedidoRequest(1L, 3));

        assertEquals(0, new BigDecimal("3000").compareTo(r.total()));
        assertEquals("Teclado", r.nombreProducto());
        verify(productoClient).descontarStock(1L, 3);
    }

    @Test
    void sinStockNoDescuentaNiGuarda() {
        when(productoClient.obtenerProducto(1L)).thenReturn(producto(2));
        assertThrows(StockInsuficienteException.class, () -> service().crear(new CrearPedidoRequest(1L, 3)));
        verify(productoClient, never()).descontarStock(any(), org.mockito.ArgumentMatchers.anyInt());
        verify(repository, never()).save(any());
    }

    @Test
    void productoInexistenteLanza404() {
        when(productoClient.obtenerProducto(9L)).thenThrow(new RecursoNoEncontradoException("producto", 9L));
        assertThrows(RecursoNoEncontradoException.class, () -> service().crear(new CrearPedidoRequest(9L, 1)));
    }

    @Test
    void productoServiceCaidoLanza503() {
        when(productoClient.obtenerProducto(1L)).thenThrow(new ServicioNoDisponibleException("producto-service"));
        assertThrows(ServicioNoDisponibleException.class, () -> service().crear(new CrearPedidoRequest(1L, 1)));
        verify(repository, never()).save(any());
    }

    @Test
    void siFallaElDescuentoNoSeGuardaElPedido() {
        when(productoClient.obtenerProducto(1L)).thenReturn(producto(10));
        doThrow(new StockInsuficienteException(1L)).when(productoClient).descontarStock(1L, 3);
        assertThrows(StockInsuficienteException.class, () -> service().crear(new CrearPedidoRequest(1L, 3)));
        verify(repository, never()).save(any());
    }
}
