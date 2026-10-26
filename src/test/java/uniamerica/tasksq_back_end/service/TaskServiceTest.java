
package uniamerica.tasksq_back_end.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import uniamerica.tasksq_back_end.entity.Task;
import uniamerica.tasksq_back_end.entity.Project;
import uniamerica.tasksq_back_end.entity.User;
import uniamerica.tasksq_back_end.dto.request.TaskRequest;
import uniamerica.tasksq_back_end.dto.mapper.TaskMapper;
import uniamerica.tasksq_back_end.entity.enums.TaskStatus;
import uniamerica.tasksq_back_end.repository.TaskRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    TaskRepository taskRepository;

    @Mock
    ProjectService projectService;

    @Mock
    UserService userService;

    @Mock
    XpService xpService;

    @Mock
    TaskMapper taskMapper;

    @InjectMocks
    TaskService taskService;


    @Test
    void findByIdTest() {
        Long id = 1L;
        Task task = new Task();

        when(taskRepository.findById(id))
                .thenReturn(Optional.of(task));

        Task resultado = taskService.findById(id);

        assertEquals(task, resultado);
        verify(taskRepository).findById(id);
    }

    @Test
    void findByIdErro() {
        Long id = 1L;

        when(taskRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            taskService.findById(id);
        });

        verify(taskRepository).findById(id);
    }

    @Test
    void deleteTaskTest() {
        Long id = 1L;
        Task task = new Task();

        when(taskRepository.findById(id))
                .thenReturn(Optional.of(task));

        taskService.deleteTask(id);

        verify(taskRepository).delete(task);
    }


    @Test
    void newTaskTest() {
        Task task = new Task();
        Project project = mock(Project.class);
        User user = mock(User.class);

        when(userService.findUser(2L))
                .thenReturn(user);

        when(userService.findUser(3L))
                .thenReturn(user);

        when(projectService.findById(1L))
                .thenReturn(project);

        when(taskRepository.save(task))
                .thenReturn(task);

        Task resultado = taskService.newTask(task, 1L, 2L, 3L);

        assertEquals(task, resultado);
        verify(taskRepository).save(task);
    }

    @Test
    void updateTaskWithoutIdTest() {
        TaskRequest request = mock(TaskRequest.class);

        when(request.id())
                .thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> {
            taskService.updateTask(request);
        });

        verify(taskRepository, never()).findByIdForUpdate(any());
    }

    @Test
    void updateTaskNotFoundTest() {
        Long id = 1L;
        TaskRequest request = mock(TaskRequest.class);

        when(request.id())
                .thenReturn(id);

        when(taskRepository.findByIdForUpdate(id))
                .thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            taskService.updateTask(request);
        });

        verify(taskRepository).findByIdForUpdate(id);
    }


    @Test
    void updateTaskTest() {
        TaskRequest request = mock(TaskRequest.class);
        Task task = new Task();

        when(request.id())
                .thenReturn(1L);

        when(request.assigneeId())
                .thenReturn(2L);

        when(request.projectId())
                .thenReturn(3L);

        when(taskRepository.findByIdForUpdate(1L))
                .thenReturn(Optional.of(task));

        when(userService.findUser(2L))
                .thenReturn(new User());

        when(projectService.findById(3L))
                .thenReturn(new Project());

        when(taskRepository.save(task))
                .thenReturn(task);

        Task resultado = taskService.updateTask(request);

        assertEquals(task, resultado);
        verify(taskRepository).save(task);
    }

    @Test
    void completeTaskTest() {
        Task task = new Task();
        User user = new User();
        task.setId(1L);
        task.setStatus(TaskStatus.ANDAMENTO);
        task.setAssigneeId(user);

        when(taskRepository.findByIdForUpdate(1L))
                .thenReturn(Optional.of(task));

        when(taskRepository.save(task))
                .thenReturn(task);

        Task resultado = taskService.completeTask(1L);

        assertEquals(TaskStatus.CONCLUIDO, resultado.getStatus());
        assertNotNull(resultado.getCompletedAt());
        verify(xpService).rewardCompletedTask(1L);
    }


    @Test
    void completeTaskNotTest() {
        Long id = 1L;

        when(taskRepository.findByIdForUpdate(id))
                .thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            taskService.completeTask(id);
        });

        verify(taskRepository).findByIdForUpdate(id);
    }

    @Test
    void startTaskTest() {
        Long id = 1L;
        Task task = new Task();
        task.setStatus(TaskStatus.CONCLUIDO);
        task.setCompletedAt(LocalDateTime.now());

        when(taskRepository.findByIdForUpdate(id))
                .thenReturn(Optional.of(task));

        when(taskRepository.save(task))
                .thenReturn(task);

        Task resultado = taskService.startTask(id);

        assertEquals(TaskStatus.ANDAMENTO, resultado.getStatus());
        assertNull(resultado.getCompletedAt());
        verify(taskRepository).save(task);
    }

    @Test
    void startTaskNotTest() {
        Long id = 1L;

        when(taskRepository.findByIdForUpdate(id))
                .thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            taskService.startTask(id);
        });

        verify(taskRepository).findByIdForUpdate(id);
    }

    @Test
    void checkStatusTest() {
        Task task = new Task();

        task.setDeadLine(LocalDate.now().minusDays(1));
        task.setStatus(TaskStatus.ANDAMENTO);

        taskService.checkStatus(List.of(task));

        assertEquals(TaskStatus.ATRASADA, task.getStatus());
        verify(taskRepository).save(task);
    }

    @Test
    void checkStatusCompletedTest() {
        Task task = new Task();

        task.setDeadLine(LocalDate.now().minusDays(1));
        task.setStatus(TaskStatus.CONCLUIDO);

        taskService.checkStatus(List.of(task));

        assertEquals(TaskStatus.CONCLUIDO, task.getStatus());
        verify(taskRepository, never()).save(task);
    }


    @Test
    void findByUserTest() {
        Long id = 1L;
        List<Task> tasks = List.of(new Task());

        when(taskRepository.findByAssigneeId_Id(id))
                .thenReturn(tasks);

        List<Task> resultado = taskService.findByUser(id);

        assertEquals(tasks, resultado);
        verify(userService).findUser(id);
        verify(taskRepository).findByAssigneeId_Id(id);
    }

    @Test
    void findByProjectTest() {
        Long id = 1L;
        List<Task> tasks = List.of(new Task());

        when(taskRepository.findByProjectId(id))
                .thenReturn(tasks);

        List<Task> resultado = taskService.findByProject(id);

        assertEquals(tasks, resultado);
        verify(projectService).findById(id);
        verify(taskRepository).findByProjectId(id);
    }

    @Test
    void loadDailyTest() {
        when(taskRepository.findAll()).thenReturn(List.of());

        taskService.loadDaily();

        verify(taskRepository).findAll();
    }

    @Test
    void loadServiceTest() {
        when(taskRepository.findAll()).thenReturn(List.of());

        taskService.loadService();

        verify(taskRepository).findAll();
    }
}
