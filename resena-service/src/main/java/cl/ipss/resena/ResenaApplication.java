package cl.ipss.resena;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Punto de entrada del microservicio de reseñas (puerto 8088). */
@SpringBootApplication
public class ResenaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResenaApplication.class, args);
    }
}
