package cl.ipss.pedido.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Prueba el Builder: valida y calcula el total. Sin Spring ni base de datos. */
class PedidoTest {

    private Pedido.Builder valido() {
        return Pedido.builder().productoId(1L).nombreProducto("Teclado")
                .precioUnitario(new BigDecimal("1000")).cantidad(3);
    }

    @Test
    void elBuilderCalculaElTotal() {
        assertEquals(0, new BigDecimal("3000").compareTo(valido().build().getTotal()));
    }

    @Test
    void rechazaCantidadCero() {
        assertThrows(IllegalStateException.class, () -> valido().cantidad(0).build());
    }

    @Test
    void rechazaProductoNulo() {
        assertThrows(IllegalStateException.class, () -> valido().productoId(null).build());
    }

    @Test
    void rechazaPrecioInvalido() {
        assertThrows(IllegalStateException.class, () -> valido().precioUnitario(BigDecimal.ZERO).build());
    }
}
