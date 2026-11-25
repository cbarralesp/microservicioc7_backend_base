package cl.ipss.producto.entity;

import cl.ipss.producto.exception.StockInsuficienteException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

/**
 * ENTITY: representa la tabla producto. Java puro, sin Lombok.
 *
 * PATRÓN BUILDER: el constructor con datos es PRIVADO; un producto solo se crea con
 * Producto.builder()...build(), que valida los datos antes de construirlo.
 * La regla del stock vive aquí: la entity protege su propio estado.
 */
@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private Integer stock;

    /** JPA lo exige para crear la entidad al leer de la base (protected: nadie más lo usa). */
    protected Producto() {
    }

    private Producto(Builder b) {
        this.nombre = b.nombre;
        this.precio = b.precio;
        this.stock = b.stock;
    }

    public static Builder builder() {
        return new Builder();
    }

    /** Regla del dominio: descuenta unidades. No se puede vender más de lo que hay. */
    public void descontarStock(int cantidad) {
        if (cantidad > stock) {
            throw new StockInsuficienteException(nombre, stock);
        }
        this.stock -= cantidad;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public Integer getStock() {
        return stock;
    }

    // =====================================================================
    //  BUILDER
    // =====================================================================
    public static class Builder {

        private String nombre;
        private BigDecimal precio;
        private int stock;

        private Builder() {
        }

        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder precio(BigDecimal precio) {
            this.precio = precio;
            return this;
        }

        public Builder stock(int stock) {
            this.stock = stock;
            return this;
        }

        /** Valida y construye. Limpia los espacios sobrantes del nombre. */
        public Producto build() {
            if (nombre == null || nombre.isBlank()) {
                throw new IllegalStateException("El nombre del producto es obligatorio");
            }
            if (precio == null || precio.signum() <= 0) {
                throw new IllegalStateException("El precio debe ser mayor a 0");
            }
            if (stock < 0) {
                throw new IllegalStateException("El stock no puede ser negativo");
            }
            nombre = nombre.trim();
            return new Producto(this);
        }
    }
}
