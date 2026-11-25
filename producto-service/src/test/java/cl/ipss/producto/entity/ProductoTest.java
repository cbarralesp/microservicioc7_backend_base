package cl.ipss.producto.entity;

import cl.ipss.producto.exception.StockInsuficienteException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Prueba el Builder y la regla del stock. Son tests unitarios puros: sin Spring ni base de datos. */
class ProductoTest {

    private Producto.Builder valido() {
        return Producto.builder().nombre("  Teclado  ").precio(new BigDecimal("45990")).stock(10);
    }

    @Test
    void builderConstruyeYLimpiaElNombre() {
        Producto p = valido().build();
        assertEquals("Teclado", p.getNombre());
        assertEquals(10, p.getStock());
    }

    @Test
    void builderRechazaNombreVacio() {
        assertThrows(IllegalStateException.class, () -> valido().nombre(" ").build());
    }

    @Test
    void builderRechazaPrecioCero() {
        assertThrows(IllegalStateException.class, () -> valido().precio(BigDecimal.ZERO).build());
    }

    @Test
    void builderRechazaStockNegativo() {
        assertThrows(IllegalStateException.class, () -> valido().stock(-1).build());
    }

    @Test
    void descontarStockResta() {
        Producto p = valido().build();
        p.descontarStock(4);
        assertEquals(6, p.getStock());
    }

    @Test
    void descontarMasDeLoQueHayFalla() {
        Producto p = valido().stock(2).build();
        assertThrows(StockInsuficienteException.class, () -> p.descontarStock(3));
        assertEquals(2, p.getStock());
    }
}
