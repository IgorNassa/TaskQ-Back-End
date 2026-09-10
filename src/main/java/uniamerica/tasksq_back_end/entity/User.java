package uniamerica.tasksq_back_end.entity;


import java.net.URI;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uniamerica.tasksq_back_end.audit.AuditableEntity;
import uniamerica.tasksq_back_end.entity.enums.EloUser;
import uniamerica.tasksq_back_end.entity.enums.UserStatus;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_user")
public class User extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private Email email;

    @Column(name = "password_hash", nullable = false, length = 20)
    private String passwordHash;

    @Column(name = "avatar_url", nullable = true)
    private URI avatarUrl;

    @Column(name = "cargo", nullable = false)
    @ManyToOne
    @JoinColumn(name = "id")
    private Cargo cargo;

    @Column(name = "status", nullable = false)
    private UserStatus status;

    @Column(name = "xp", nullable = false)
    private Long xp;

    @Column(name = "elo", nullable = false)
    private EloUser elo;
}