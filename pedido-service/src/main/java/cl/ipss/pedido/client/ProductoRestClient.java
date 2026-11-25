package cl.ipss.pedido.client;

import cl.ipss.pedido.dto.ProductoRemoto;
import cl.ipss.pedido.exception.RecursoNoEncontradoException;
import cl.ipss.pedido.exception.ServicioNoDisponibleException;
import cl.ipss.pedido.exception.StockInsuficienteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Map;

/** Implementación HTTP de ProductoClient. Traduce los errores remotos a excepciones propias. */
@Component
public class ProductoRestClient implements ProductoClient {

    private static final Logger log = LoggerFactory.getLogger(ProductoRestClient.class);

    private final RestClient restClient;

    public ProductoRestClient(RestClient productoHttpClient) {
        this.restClient = productoHttpClient;
    }

    @Override
    public ProductoRemoto obtenerProducto(Long productoId) {
        log.info("--- calling producto-service: GET /api/productos/{} ---", productoId);
        try {
            return restClient.get()
                    .uri("/api/productos/{id}", productoId)
                    .retrieve()
                    .body(ProductoRemoto.class);
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new RecursoNoEncontradoException("producto", productoId);
            }
            throw ex;
        } catch (RestClientException ex) {
            throw servicioCaido(ex);
        }
    }

    @Override
    public void descontarStock(Long productoId, int cantidad) {
        log.info("--- calling producto-service: PATCH /api/productos/{}/descontar-stock ---", productoId);
        try {
            restClient.patch()
                    .uri("/api/productos/{id}/descontar-stock", productoId)
                    .body(Map.of("cantidad", cantidad))
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.CONFLICT) {
                throw new StockInsuficienteException(productoId);
            }
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new RecursoNoEncontradoException("producto", productoId);
            }
            throw ex;
        } catch (RestClientException ex) {
            throw servicioCaido(ex);
        }
    }

    private ServicioNoDisponibleException servicioCaido(RestClientException ex) {
        log.error("--- producto-service unavailable: {} ---", ex.getMessage());
        return new ServicioNoDisponibleException("producto-service");
    }
}
