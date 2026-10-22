package uniamerica.tasksq_back_end.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;

import org.springframework.web.server.ResponseStatusException;
import uniamerica.tasksq_back_end.entity.Project;
import uniamerica.tasksq_back_end.entity.enums.TaskStatus;
import uniamerica.tasksq_back_end.repository.ProjectRepository;
import uniamerica.tasksq_back_end.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void deleteProjectTest(){
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
    void deleteProjectTask(){
        Long id = 1L;
        Project project = new Project();

        when(projectRepository.findById(id))
                .thenReturn(Optional.of(project));

        when(taskRepository.existsByProjectId(id))
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
    void updateProject(){
        Project project = new Project();
        project.setStartDate(LocalDate.now());
        project.setDeadLine(LocalDate.of(2028,12,12));

        when(projectRepository.save(project))
                .thenReturn(project);

        Project aux = projectService.updateProject(project);
        verify(projectRepository).save(project);
        assertEquals(project, aux);
    }

    @Test
    void LoadDailyTest(){
        when(projectRepository.findAll()).thenReturn(List.of());

        projectService.loadDaily();

        verify(projectRepository).findAll();
    }

    @Test
    void startProject(){
        Long id = 1L;
        Project project = new Project();

        when(projectRepository.findById(id))
                .thenReturn(Optional.of(project));

        when(projectRepository.save(project))
                .thenReturn(project);

        Project projectStart = projectService.startProject(id);

        verify(projectRepository).save(project);
        assertEquals(TaskStatus.ANDAMENTO, projectStart.getStatus());
    }

    @Test
    void completedProject(){
        Long id = 1L;
        Project project = new Project();

        when(projectRepository.findById(id))
                .thenReturn(Optional.of(project));

        when(projectRepository.save(project))
                .thenReturn(project);

        Project projectComplet = projectService.completedProject(id);

        verify(projectRepository).save(project);
        assertEquals(TaskStatus.CONCLUIDO, projectComplet.getStatus());
    }

    @Test
    void checkStatus(){
        Project project = new Project();
        project.setDeadLine(LocalDate.now().minusDays(1));
        project.setStatus(TaskStatus.ANDAMENTO);
        projectService.checkStatus(List.of(project));

        assertEquals(TaskStatus.ATRASADA, project.getStatus());
        verify(projectRepository).save(project);
    }

    @Test
    void projectNotEmpty(){
        Long id = 1L;
        Project project = new Project();
        when(projectRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            projectService.findById(id);
        });

        verify(projectRepository).findById(id);
    }

    @Test
    void CompletedProjectNotEmpty(){
        Long id = 1L;

        when(projectRepository.findById(id))
                .thenReturn(Optional.empty());


        assertThrows(ResponseStatusException.class, () -> {
            projectService.completedProject(id);
        });

        verify(projectRepository).findById(id);
    }

    @Test
    void StartProjectNotEmpty(){
        Long id = 1L;

        when(projectRepository.findById(id))
                .thenReturn(Optional.empty());


        assertThrows(ResponseStatusException.class, () -> {
            projectService.startProject(id);
        });

        verify(projectRepository).findById(id);
    }
}
