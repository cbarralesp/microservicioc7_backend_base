package cl.ipss.producto.repository;

import cl.ipss.producto.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

/** Acceso a datos: Spring Data genera la implementación (findAll, findById, save...). */
public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
