package uniamerica.tasksq_back_end.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import uniamerica.tasksq_back_end.entity.User;

public interface LoginRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailIgnoreCase(String email);
}
