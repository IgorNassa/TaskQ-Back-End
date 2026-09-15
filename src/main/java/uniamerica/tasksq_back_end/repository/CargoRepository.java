package uniamerica.tasksq_back_end.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uniamerica.tasksq_back_end.entity.Cargo;

public interface CargoRepository extends JpaRepository<Cargo, Long> {

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}
