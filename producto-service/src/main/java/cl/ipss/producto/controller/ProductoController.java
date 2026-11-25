package cl.ipss.producto.controller;

import cl.ipss.producto.dto.CrearProductoRequest;
import cl.ipss.producto.dto.DescontarStockRequest;
import cl.ipss.producto.dto.ProductoResponse;
import cl.ipss.producto.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

/** Capa web: recibe HTTP, delega al service y devuelve el código correcto. Sin lógica de negocio. */
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProductoResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ProductoResponse obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody CrearProductoRequest request) {
        ProductoResponse creado = service.crear(request);
        return ResponseEntity.created(URI.create("/api/productos/" + creado.id())).body(creado);
    }

    /** Lo llama pedido-service cuando alguien compra. */
    @PatchMapping("/{id}/descontar-stock")
    public ProductoResponse descontarStock(@PathVariable Long id,
                                           @Valid @RequestBody DescontarStockRequest request) {
        return service.descontarStock(id, request.cantidad());
    }
}
