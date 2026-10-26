package uniamerica.tasksq_back_end.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uniamerica.tasksq_back_end.dto.mapper.TaskMapper;
import uniamerica.tasksq_back_end.dto.request.TaskRequest;
import uniamerica.tasksq_back_end.entity.Task;
import uniamerica.tasksq_back_end.service.TaskService;

import java.util.List;

import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class TaskControllerTest {
    @Mock
    TaskMapper taskMapper;

    @Mock
    TaskService taskService;

    @InjectMocks
    TaskController taskController;

    @Test
    void createTest() {
        TaskRequest request = mock(TaskRequest.class);
        Task task = new Task();
        Task savedTask = new Task();

        when(taskMapper.toEntity(request))
                .thenReturn(task);

        when(taskService.newTask(task, request.projectId(), request.assigneeId(), request.creatorId()))
                .thenReturn(savedTask);

        taskController.create(request);

        verify(taskMapper).toEntity(request);
        verify(taskService).newTask(task, request.projectId(), request.assigneeId(), request.creatorId());
    }

    @Test
    void updateTest() {
        TaskRequest request = mock(TaskRequest.class);
        Task task = new Task();

        when(taskService.updateTask(request))
                .thenReturn(task);

        taskController.update(request);

        verify(taskService).updateTask(request);
    }

    @Test
    void findAllTest() {
        when(taskService.findAll())
                .thenReturn(List.of());

        taskController.findAll();

        verify(taskService).findAll();
    }

    @Test
    void findByIdTest() {
        Long id = 1L;
        Task task = new Task();

        when(taskService.findById(id))
                .thenReturn(task);

        taskController.findById(id);

        verify(taskService).findById(id);
    }

    @Test
    void completedTest() {
        Long id = 1L;
        Task task = new Task();

        when(taskService.completeTask(id))
                .thenReturn(task);

        taskController.completed(id);

        verify(taskService).completeTask(id);
    }

    @Test
    void startedTest() {
        Long id = 1L;
        Task task = new Task();

        when(taskService.startTask(id))
                .thenReturn(task);

        taskController.started(id);

        verify(taskService).startTask(id);
    }

    @Test
    void deleteTest() {
        Long id = 1L;

        taskController.delete(id);

        verify(taskService).deleteTask(id);
    }

    @Test
    void findByUserTest() {
        Long id = 1L;

        when(taskService.findByUser(id))
                .thenReturn(List.of());

        taskController.findByUser(id);

        verify(taskService).findByUser(id);
    }

    @Test
    void findByProjectTest() {
        Long id = 1L;

        when(taskService.findByProject(id))
                .thenReturn(List.of());

        taskController.findByProject(id);

        verify(taskService).findByProject(id);
    }
}
