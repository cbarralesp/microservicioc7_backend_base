package cl.ipss.producto.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Datos de la cabecera de Swagger UI (http://localhost:8086/swagger-ui.html). */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI().info(new Info()
                .title("API producto-service")
                .description("Productos y control de stock - Java 21, Spring Boot, PostgreSQL")
                .version("1.0.0"));
    }
}
