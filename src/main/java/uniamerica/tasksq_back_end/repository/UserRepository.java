package uniamerica.tasksq_back_end.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uniamerica.tasksq_back_end.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    boolean existsByCargoId(Long cargoId);
}
