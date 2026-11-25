package cl.ipss.resena.client;

import cl.ipss.resena.dto.ProductoRemoto;
import cl.ipss.resena.exception.RecursoNoEncontradoException;
import cl.ipss.resena.exception.ServicioNoDisponibleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

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
            log.error("--- producto-service unavailable: {} ---", ex.getMessage());
            throw new ServicioNoDisponibleException("producto-service");
        }
    }
}
