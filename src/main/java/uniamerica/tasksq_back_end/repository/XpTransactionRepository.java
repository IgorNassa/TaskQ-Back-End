package uniamerica.tasksq_back_end.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uniamerica.tasksq_back_end.entity.XpTransaction;

public interface XpTransactionRepository extends JpaRepository<XpTransaction, Long> {
    boolean existsByTipoReferenciaAndReferenciaId(String tipoReferencia, Long referenciaId);
}
