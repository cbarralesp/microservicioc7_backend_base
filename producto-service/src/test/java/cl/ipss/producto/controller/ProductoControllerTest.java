package cl.ipss.producto.controller;

import cl.ipss.producto.dto.ProductoResponse;
import cl.ipss.producto.exception.RecursoNoEncontradoException;
import cl.ipss.producto.exception.StockInsuficienteException;
import cl.ipss.producto.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Prueba solo la capa web (rutas, validaciones y códigos HTTP) con el service simulado. */
@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ProductoService service;

    private ProductoResponse ejemplo() {
        return new ProductoResponse(1L, "Teclado", new BigDecimal("45990"), 10);
    }

    @Test
    void listaProductos() throws Exception {
        when(service.listar()).thenReturn(List.of(ejemplo()));
        mvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Teclado"));
    }

    @Test
    void obtenerInexistenteDevuelve404() throws Exception {
        when(service.obtener(anyLong())).thenThrow(new RecursoNoEncontradoException(9L));
        mvc.perform(get("/api/productos/9")).andExpect(status().isNotFound());
    }

    @Test
    void crearDevuelve201ConLocation() throws Exception {
        when(service.crear(any())).thenReturn(ejemplo());
        mvc.perform(post("/api/productos").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Teclado\",\"precio\":45990,\"stock\":10}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/productos/1"));
    }

    @Test
    void crearConDatosInvalidosDevuelve400() throws Exception {
        mvc.perform(post("/api/productos").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"\",\"precio\":-5,\"stock\":10}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void descontarSinStockDevuelve409() throws Exception {
        when(service.descontarStock(anyLong(), anyInt())).thenThrow(new StockInsuficienteException("Teclado", 0));
        mvc.perform(patch("/api/productos/1/descontar-stock").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cantidad\":3}"))
                .andExpect(status().isConflict());
    }
}
