package cl.ipss.resena.controller;

import cl.ipss.resena.dto.ResenaResponse;
import cl.ipss.resena.service.ResenaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Capa web sola: comprueba rutas, validaciones y códigos HTTP. */
@WebMvcTest(ResenaController.class)
class ResenaControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ResenaService service;

    @Test
    void crearResponde201ConLaUbicacion() throws Exception {
        when(service.crear(any())).thenReturn(
                new ResenaResponse("abc123", 1L, "Teclado", "Camila", 5, "Muy bueno", LocalDateTime.now()));

        mvc.perform(post("/api/resenas").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productoId\":1,\"autor\":\"Camila\",\"estrellas\":5,\"comentario\":\"Muy bueno\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/resenas/abc123"));
    }

    @Test
    void crearConDatosInvalidosResponde400() throws Exception {
        mvc.perform(post("/api/resenas").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productoId\":1,\"autor\":\"\",\"estrellas\":9,\"comentario\":\"x\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void conLaBaseCaidaResponde503() throws Exception {
        when(service.listar(null)).thenThrow(new DataAccessResourceFailureException("sin conexión"));

        mvc.perform(get("/api/resenas")).andExpect(status().isServiceUnavailable());
    }
}
