package cl.ipss.pedido.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Datos de la cabecera de Swagger UI (http://localhost:8087/swagger-ui.html). */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI().info(new Info()
                .title("API pedido-service")
                .description("Pedidos que consultan a producto-service - Java 21, Spring Boot, PostgreSQL")
                .version("1.0.0"));
    }
}
