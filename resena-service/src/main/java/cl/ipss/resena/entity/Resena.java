package cl.ipss.resena.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * DOCUMENTO: representa un documento de la colección "resenas". Java puro, sin Lombok.
 *
 * Comparado con una entity de JPA:
 *   - @Document en lugar de @Entity y @Table; no hace falta @Column.
 *   - El id es un String que genera MongoDB (ObjectId), no un número autoincremental.
 *   - La base no valida nada (no hay NOT NULL ni CHECK): las reglas viven en el Builder.
 *
 * El producto vive en la base de OTRO microservicio: se guarda su id y una copia del nombre.
 */
@Document(collection = "resenas")
public class Resena {

    @Id
    private String id;

    private Long productoId;
    private String nombreProducto;
    private String autor;
    private Integer estrellas;
    private String comentario;
    private LocalDateTime creadoEn;

    /** Spring Data lo usa para crear el objeto al leer de la base. */
    protected Resena() {
    }

    private Resena(Builder b) {
        this.productoId = b.productoId;
        this.nombreProducto = b.nombreProducto;
        this.autor = b.autor;
        this.estrellas = b.estrellas;
        this.comentario = b.comentario;
        // MongoDB guarda las fechas con precisión de milisegundos
        this.creadoEn = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getId() {
        return id;
    }

    public Long getProductoId() {
        return productoId;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public String getAutor() {
        return autor;
    }

    public Integer getEstrellas() {
        return estrellas;
    }

    public String getComentario() {
        return comentario;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    // =====================================================================
    //  BUILDER
    // =====================================================================
    public static class Builder {

        private Long productoId;
        private String nombreProducto;
        private String autor;
        private int estrellas;
        private String comentario;

        private Builder() {
        }

        public Builder productoId(Long productoId) {
            this.productoId = productoId;
            return this;
        }

        public Builder nombreProducto(String nombreProducto) {
            this.nombreProducto = nombreProducto;
            return this;
        }

        public Builder autor(String autor) {
            this.autor = autor;
            return this;
        }

        public Builder estrellas(int estrellas) {
            this.estrellas = estrellas;
            return this;
        }

        public Builder comentario(String comentario) {
            this.comentario = comentario;
            return this;
        }

        /** Valida y construye. Limpia los espacios sobrantes de los textos. */
        public Resena build() {
            if (productoId == null) {
                throw new IllegalStateException("El producto de la reseña es obligatorio");
            }
            if (nombreProducto == null || nombreProducto.isBlank()) {
                throw new IllegalStateException("El nombre del producto es obligatorio");
            }
            if (autor == null || autor.isBlank()) {
                throw new IllegalStateException("El autor es obligatorio");
            }
            if (estrellas < 1 || estrellas > 5) {
                throw new IllegalStateException("Las estrellas van de 1 a 5");
            }
            if (comentario == null || comentario.isBlank()) {
                throw new IllegalStateException("El comentario es obligatorio");
            }
            autor = autor.trim();
            comentario = comentario.trim();
            return new Resena(this);
        }
    }
}
