package cl.ipss.resena.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Datos de la cabecera de Swagger UI (http://localhost:8088/swagger-ui.html). */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI().info(new Info()
                .title("API resena-service")
                .description("Reseñas de productos - Java 21, Spring Boot, MongoDB")
                .version("1.0.0"));
    }
}
