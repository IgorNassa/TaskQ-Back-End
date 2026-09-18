package uniamerica.tasksq_back_end.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uniamerica.tasksq_back_end.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying
    @Query("update User u set u.xp = u.xp + :amount, u.updatedAt = CURRENT_TIMESTAMP where u.id = :id")
    int incrementXp(@Param("id") Long id, @Param("amount") long amount);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    boolean existsByCargoId(Long cargoId);
}
