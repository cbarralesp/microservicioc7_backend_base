package cl.ipss.resena.controller;

import cl.ipss.resena.dto.CrearResenaRequest;
import cl.ipss.resena.dto.ResenaResponse;
import cl.ipss.resena.service.ResenaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

/** Capa web de reseñas: sin lógica de negocio, solo HTTP. */
@RestController
@RequestMapping("/api/resenas")
public class ResenaController {

    private final ResenaService service;

    public ResenaController(ResenaService service) {
        this.service = service;
    }

    /** GET /api/resenas o GET /api/resenas?productoId=1 */
    @GetMapping
    public List<ResenaResponse> listar(@RequestParam(required = false) Long productoId) {
        return service.listar(productoId);
    }

    @PostMapping
    public ResponseEntity<ResenaResponse> crear(@Valid @RequestBody CrearResenaRequest request) {
        ResenaResponse creada = service.crear(request);
        return ResponseEntity.created(URI.create("/api/resenas/" + creada.id())).body(creada);
    }
}
