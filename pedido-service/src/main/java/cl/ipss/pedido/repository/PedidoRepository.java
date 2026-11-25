package cl.ipss.pedido.repository;

import cl.ipss.pedido.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/** Acceso a datos. El método se genera solo a partir de su nombre (query method). */
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findAllByOrderByCreadoEnDesc();
}
