package uniamerica.tasksq_back_end.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uniamerica.tasksq_back_end.dto.mapper.TaskMapper;
import uniamerica.tasksq_back_end.dto.request.TaskRequest;
import uniamerica.tasksq_back_end.dto.response.TaskResponse;
import uniamerica.tasksq_back_end.entity.Task;
import uniamerica.tasksq_back_end.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("api/task")
@CrossOrigin("*")
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskMapper taskMapper;

    @PostMapping("create")
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody TaskRequest request){
        Task task = taskMapper.toEntity(request);
        Task savedTask = taskService.newTask(task, request.projectId(), request.assigneeId(), request.creatorId());
        return ResponseEntity.created(URI.create("/api/task/" + savedTask.getId())).body(taskMapper.toResponse(savedTask));
    }

    @PutMapping("update")
    public ResponseEntity<TaskResponse> update(@Valid @RequestBody TaskRequest request) {
        Task updatedTask = taskService.updateTask(request);

        return ResponseEntity.ok(taskMapper.toResponse(updatedTask));
    }

    @GetMapping("all")
    public ResponseEntity<List<TaskResponse>> findAll() {
        List<Task> tasks = taskService.findAll();
        List<TaskResponse> response = tasks.stream()
                .map(taskMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> findById(@PathVariable @Positive Long id) {
        Task task = taskService.findById(id);
        return ResponseEntity.ok(taskMapper.toResponse(task));
    }

    @PutMapping("/completed/{id}")
    public ResponseEntity<TaskResponse> completed(@PathVariable @Positive Long id) {
        Task task = taskService.completeTask(id);
        return ResponseEntity.ok(taskMapper.toResponse(task));
    }

    @PutMapping("/start/{id}")
    public ResponseEntity<TaskResponse> started(@PathVariable @Positive Long id) {
        Task task = taskService.startTask(id);
        TaskResponse response = taskMapper.toResponse(task);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Positive Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("user/{id}")
    public ResponseEntity<List<TaskResponse>> findByUser(@PathVariable @Positive Long id) {
        List<Task> tasks = taskService.findByUser(id);
        List<TaskResponse> response = tasks.stream()
                .map(taskMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("project/{id}")
    public ResponseEntity<List<TaskResponse>> findByProject(@PathVariable @Positive Long id) {
        List<Task> tasks = taskService.findByProject(id);
        List<TaskResponse> response = tasks.stream()
                .map(taskMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }
}
