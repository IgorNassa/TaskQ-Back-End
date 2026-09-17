package uniamerica.tasksq_back_end.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uniamerica.tasksq_back_end.entity.Project;
import uniamerica.tasksq_back_end.entity.Task;
import uniamerica.tasksq_back_end.entity.enums.TaskPriority;
import uniamerica.tasksq_back_end.entity.enums.TaskStatus;
import uniamerica.tasksq_back_end.repository.ProjectRepository;
import uniamerica.tasksq_back_end.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    private Project saveProject(Project project){
        return projectRepository.save(project);
    }

    public Project findById(Long id){
        return projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Projeto nao encontrado"));
    }

    public List<Project> findAll(){
        return projectRepository.findAll();
    }

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
}
