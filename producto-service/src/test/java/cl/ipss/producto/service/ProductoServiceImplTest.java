package cl.ipss.producto.service;

import cl.ipss.producto.dto.CrearProductoRequest;
import cl.ipss.producto.dto.ProductoResponse;
import cl.ipss.producto.entity.Producto;
import cl.ipss.producto.exception.RecursoNoEncontradoException;
import cl.ipss.producto.exception.StockInsuficienteException;
import cl.ipss.producto.mapper.ProductoMapper;
import cl.ipss.producto.repository.ProductoRepository;
import cl.ipss.producto.service.impl.ProductoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/** El repository se simula con Mockito: se prueba solo la lógica del service. */
@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository repository;

    private ProductoService service() {
        return new ProductoServiceImpl(repository, new ProductoMapper());
    }

    private Producto producto(int stock) {
        return Producto.builder().nombre("Mouse").precio(new BigDecimal("15990")).stock(stock).build();
    }

    @Test
    void crearGuardaYDevuelveElProducto() {
        when(repository.save(any(Producto.class))).thenAnswer(inv -> inv.getArgument(0));
        ProductoResponse r = service().crear(new CrearProductoRequest("Mouse", new BigDecimal("15990"), 5));
        assertEquals("Mouse", r.nombre());
        assertEquals(5, r.stock());
    }

    @Test
    void obtenerInexistenteLanza404() {
        when(repository.findById(9L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> service().obtener(9L));
    }

    @Test
    void descontarStockResta() {
        when(repository.findById(1L)).thenReturn(Optional.of(producto(10)));
        assertEquals(7, service().descontarStock(1L, 3).stock());
    }

    @Test
    void descontarSinStockLanza409() {
        when(repository.findById(1L)).thenReturn(Optional.of(producto(1)));
        assertThrows(StockInsuficienteException.class, () -> service().descontarStock(1L, 3));
    }
}
