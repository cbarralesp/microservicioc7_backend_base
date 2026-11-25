package cl.ipss.pedido.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * ENTITY: representa la tabla pedido. Java puro, sin Lombok.
 *
 * Esta base NO tiene FOREIGN KEY hacia producto: el producto vive en OTRA base de datos
 * (la de producto-service). Por eso se guarda producto_id como número simple y una copia
 * del nombre y del precio al momento de comprar.
 *
 * PATRÓN BUILDER: el total NO lo manda nadie, lo calcula el Builder (precio x cantidad).
 */
@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @Column(name = "nombre_producto", nullable = false, length = 100)
    private String nombreProducto;

    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private LocalDateTime creadoEn;

    /** JPA lo exige para crear la entidad al leer de la base. */
    protected Pedido() {
    }

    private Pedido(Builder b) {
        this.productoId = b.productoId;
        this.nombreProducto = b.nombreProducto;
        this.precioUnitario = b.precioUnitario;
        this.cantidad = b.cantidad;
        // TODO 2.2: calcular el total = precioUnitario x cantidad (BigDecimal.multiply)
    }

    public static Builder builder() {
        return new Builder();
    }

    @PrePersist
    void antesDeGuardar() {
        this.creadoEn = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getProductoId() {
        return productoId;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public BigDecimal getTotal() {
        return total;
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
        private BigDecimal precioUnitario;
        private int cantidad;

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

        public Builder precioUnitario(BigDecimal precioUnitario) {
            this.precioUnitario = precioUnitario;
            return this;
        }

        public Builder cantidad(int cantidad) {
            this.cantidad = cantidad;
            return this;
        }

        /** Valida y construye; el total se calcula dentro del constructor privado. */
        public Pedido build() {
            // TODO 2.1: validar con IllegalStateException (mira como lo hace Producto.Builder en producto-service):
            //   - productoId obligatorio              "El producto del pedido es obligatorio"
            //   - nombreProducto no vacío             "El nombre del producto es obligatorio"
            //   - precioUnitario mayor a 0            "El precio unitario debe ser mayor a 0"
            //   - cantidad al menos 1                 "La cantidad debe ser al menos 1"
            return new Pedido(this);
        }
    }
}
