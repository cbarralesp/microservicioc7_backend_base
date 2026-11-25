package cl.ipss.resena.repository;

import cl.ipss.resena.entity.Resena;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/** Igual que con JPA: Spring arma la consulta a partir del nombre del método. */
public interface ResenaRepository extends MongoRepository<Resena, String> {

    List<Resena> findAllByOrderByCreadoEnDesc();

    List<Resena> findByProductoIdOrderByCreadoEnDesc(Long productoId);
}
