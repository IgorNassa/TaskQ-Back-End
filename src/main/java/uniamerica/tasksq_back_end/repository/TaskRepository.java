package uniamerica.tasksq_back_end.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import uniamerica.tasksq_back_end.entity.Task;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select t from Task t where t.id = :id")
    Optional<Task> findByIdForUpdate(@Param("id") Long id);

    List<Task> findByAssigneeId(Long assigneeId);
    List<Task> findByProjectId(Long projectId);
}
