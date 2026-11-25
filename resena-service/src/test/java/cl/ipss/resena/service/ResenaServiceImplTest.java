package cl.ipss.resena.service;

import cl.ipss.resena.client.ProductoClient;
import cl.ipss.resena.dto.CrearResenaRequest;
import cl.ipss.resena.dto.ProductoRemoto;
import cl.ipss.resena.dto.ResenaResponse;
import cl.ipss.resena.entity.Resena;
import cl.ipss.resena.exception.RecursoNoEncontradoException;
import cl.ipss.resena.mapper.ResenaMapper;
import cl.ipss.resena.repository.ResenaRepository;
import cl.ipss.resena.service.impl.ResenaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** producto-service y la base se simulan: se prueba solo la lógica de la reseña. */
@ExtendWith(MockitoExtension.class)
class ResenaServiceImplTest {

    @Mock
    private ResenaRepository repository;

    @Mock
    private ProductoClient productoClient;

    private ResenaService service() {
        return new ResenaServiceImpl(repository, productoClient, new ResenaMapper());
    }

    private final CrearResenaRequest request = new CrearResenaRequest(1L, "Camila", 5, "Muy bueno");

    @Test
    void guardaLaResenaConElNombreDelProducto() {
        when(productoClient.obtenerProducto(1L)).thenReturn(new ProductoRemoto(1L, "Teclado"));
        when(repository.save(any(Resena.class))).thenAnswer(i -> i.getArgument(0));

        ResenaResponse respuesta = service().crear(request);

        assertEquals("Teclado", respuesta.nombreProducto());
        assertEquals(5, respuesta.estrellas());
    }

    @Test
    void noGuardaSiElProductoNoExiste() {
        when(productoClient.obtenerProducto(1L)).thenThrow(new RecursoNoEncontradoException("producto", 1L));

        assertThrows(RecursoNoEncontradoException.class, () -> service().crear(request));

        verify(repository, never()).save(any());
    }

    @Test
    void filtraPorProductoSoloCuandoSeIndica() {
        service().listar(1L);
        service().listar(null);

        verify(repository).findByProductoIdOrderByCreadoEnDesc(1L);
        verify(repository).findAllByOrderByCreadoEnDesc();
    }
}
