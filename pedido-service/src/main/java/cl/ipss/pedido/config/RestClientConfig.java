package cl.ipss.pedido.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;

/** Crea el cliente HTTP hacia producto-service con su dirección y un tiempo máximo de espera. */
@Configuration
public class RestClientConfig {

    /**
     * Se usa el HttpClient nativo de Java porque soporta PATCH (HttpURLConnection, el cliente por
     * defecto de RestClient, falla con «Invalid HTTP method: PATCH»).
     * El bean NO se llama productoRestClient para no chocar con el @Component ProductoRestClient.
     */
    @Bean
    public RestClient productoHttpClient(@Value("${app.producto-service.url}") String url,
                                         @Value("${app.producto-service.timeout-ms}") long timeoutMs) {
        HttpClient http = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(timeoutMs))
                .build();
        JdkClientHttpRequestFactory fabrica = new JdkClientHttpRequestFactory(http);
        fabrica.setReadTimeout(Duration.ofMillis(timeoutMs));
        return RestClient.builder()
                .baseUrl(url)
                .requestFactory(fabrica)
                .build();
    }
}
