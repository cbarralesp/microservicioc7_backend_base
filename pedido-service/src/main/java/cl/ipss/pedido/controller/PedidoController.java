package cl.ipss.pedido.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.ipss.pedido.dto.CrearPedidoRequest;
import cl.ipss.pedido.dto.PedidoResponse;
import cl.ipss.pedido.service.PedidoService;
import jakarta.validation.Valid;

/** Capa web de pedidos: sin lógica de negocio, solo HTTP. */
@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    // TODO 4.1: GET /api/pedidos (@GetMapping) -> devolver service.listar()
    @GetMapping
    public List<PedidoResponse> listar() {
        return service.listar();
    }

    // TODO 4.2: POST /api/pedidos (@PostMapping) -> llamar service.crear(request) y responder 201 con
    //           ResponseEntity.created(URI.create("/api/pedidos/" + id)).body(...)
    //           No olvides @Valid @RequestBody en el parámetro.
    @PostMapping
    public ResponseEntity<PedidoResponse> crear(@Valid @RequestBody CrearPedidoRequest request) {
        PedidoResponse creado = service.crear(request);
        return ResponseEntity.created(URI.create("/api/pedidos/" + creado.id())).body(creado);
    }
}
