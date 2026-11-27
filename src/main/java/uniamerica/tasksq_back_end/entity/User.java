package uniamerica.tasksq_back_end.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uniamerica.tasksq_back_end.audit.AuditableEntity;
import uniamerica.tasksq_back_end.entity.enums.UserProfile;
import uniamerica.tasksq_back_end.entity.enums.UserRank;
import uniamerica.tasksq_back_end.entity.enums.UserStatus;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_user")
public class User extends AuditableEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 250)
    private String nome;

    @Column(name = "email", nullable = false, unique = true, length = 250)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 250)
    private String senhaHash;

    @Column(name = "avatar_url", length = 500)
    private String urlAvatar;

    @ManyToOne
    @JoinColumn(name = "cargo_id", nullable = false)
    private Role cargo;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "xp", nullable = false)
    private Long xp = 0L;

    @Column(name = "elo", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRank elo = UserRank.INICIANTE;

    @Column(name = "perfil", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private UserProfile perfil = UserProfile.MEMBRO;

    @OneToMany(mappedBy = "assigneeId")
    private List<Task> tasks;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + perfil.name()));
    }

    @Override
    public String getPassword() {
        return senhaHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return status == UserStatus.ATIVO;
    }
}
