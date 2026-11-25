package cl.ipss.resena.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Reglas del Builder: no necesita MongoDB ni Spring. */
class ResenaTest {

    private Resena.Builder valida() {
        return Resena.builder()
                .productoId(1L)
                .nombreProducto("Teclado")
                .autor("  Camila  ")
                .estrellas(5)
                .comentario("  Muy bueno  ");
    }

    @Test
    void construyeYLimpiaLosTextos() {
        Resena resena = valida().build();

        assertEquals("Camila", resena.getAutor());
        assertEquals("Muy bueno", resena.getComentario());
        assertNotNull(resena.getCreadoEn());
    }

    @Test
    void rechazaEstrellasFueraDeRango() {
        assertThrows(IllegalStateException.class, () -> valida().estrellas(0).build());
        assertThrows(IllegalStateException.class, () -> valida().estrellas(6).build());
    }

    @Test
    void rechazaAutorVacio() {
        assertThrows(IllegalStateException.class, () -> valida().autor("   ").build());
    }

    @Test
    void rechazaComentarioVacio() {
        assertThrows(IllegalStateException.class, () -> valida().comentario(null).build());
    }
}
