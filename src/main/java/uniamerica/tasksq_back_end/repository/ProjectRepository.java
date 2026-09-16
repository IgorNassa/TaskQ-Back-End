package uniamerica.tasksq_back_end.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uniamerica.tasksq_back_end.entity.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
}