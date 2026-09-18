package uniamerica.tasksq_back_end.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uniamerica.tasksq_back_end.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    boolean existsByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);
}
