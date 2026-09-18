package uniamerica.tasksq_back_end.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uniamerica.tasksq_back_end.audit.AuditableEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_xp_transaction")
public class XpTransaction extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long usuarioId;

    @Column(name = "amount", nullable = false)
    private Integer quantidade;

    @Column(name = "reason", nullable = false, length = 250)
    private String motivo;

    @Column(name = "reference_type", length = 100)
    private String tipoReferencia;

    @Column(name = "reference_id")
    private Long referenciaId;
}