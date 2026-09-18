package uniamerica.tasksq_back_end.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uniamerica.tasksq_back_end.entity.Project;
import uniamerica.tasksq_back_end.entity.Task;
import uniamerica.tasksq_back_end.entity.enums.TaskStatus;
import uniamerica.tasksq_back_end.repository.ProjectRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Transactional
    private Project saveProject(Project project){
        return projectRepository.save(project);
    }

    public Project findById(Long id){
        return projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Projeto nao encontrado"));
    }

    public List<Project> findAll(){
        return projectRepository.findAll();
    }

    @Transactional
    public void deleteProject(Long id){
        projectRepository.deleteById(id);
    }

    public Project newProject(Project project){
        return saveProject(project);
    }

    public Project updateProject(Project project){
        return saveProject(project);
    }

    public Project completedProject(Long id){
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));

        project.setStatus(TaskStatus.CONCLUIDO);
        return projectRepository.save(project);
    }

    public Project startProject(Long id){
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));

        project.setStatus(TaskStatus.ANDAMENTO);
        return projectRepository.save(project);
    }

    public void checkStatus(List<Project> projects){
        for (Project project : projects) {
            if (project.getDeadLine().isBefore(LocalDate.now()) && project.getStatus() != TaskStatus.CONCLUIDO) {
                project.setStatus(TaskStatus.ATRASADA);
                projectRepository.save(project);
            }
        }
    }

    @PostConstruct
    public void loadService() {
        checkStatus(findAll());
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void loadDaily() {
        checkStatus(findAll());
    }
}
