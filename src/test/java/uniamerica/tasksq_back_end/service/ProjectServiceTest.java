package uniamerica.tasksq_back_end.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import uniamerica.tasksq_back_end.entity.Project;
import uniamerica.tasksq_back_end.repository.ProjectRepository;
import uniamerica.tasksq_back_end.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {
    @Mock
    ProjectRepository projectRepository;

    @Mock
    TaskRepository taskRepository;

    @Mock
    UserService userService;

    @InjectMocks
    ProjectService projectService;

    @Test
    void deletarProjetoTest(){
        Long id = 1L;
        Project project = new Project();
        when(projectRepository.findById(id))
                .thenReturn(Optional.of(project));

        when(taskRepository.existsByProjectId((id)))
                .thenReturn(false);

        projectService.deleteProject(id);
        verify(projectRepository).delete(project);
    }

    @Test
    void saveProjectServiceTest() {
        Project project = new Project();
        project.setStartDate(LocalDate.now());
        project.setDeadLine(LocalDate.of(2028,12,12));

        when(projectRepository.save(project))
                .thenReturn(project);

        Project resultado = projectService.newProject(project);

        verify(projectRepository).save(project);
    }

    @Test

    void LoadDailyTest(){
        when(projectRepository.findAll()).thenReturn(List.of());

        projectService.loadDaily();

        verify(projectRepository).findAll();
    }
}
