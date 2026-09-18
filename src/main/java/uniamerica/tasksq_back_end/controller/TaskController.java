package uniamerica.tasksq_back_end.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        try {
            Task task = taskMapper.toEntity(request);
            taskService.newTask(task, request.projectId(), request.assigneeId());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }

    @PutMapping("update")
    public ResponseEntity<TaskResponse> update(@Valid @RequestBody TaskRequest request) {
        try {
            Task task = taskService.findById(request.id());

            taskMapper.updateEntity(request, task);

            Task updatedTask = taskService.updateTask(task);

            return ResponseEntity.ok(taskMapper.toResponse(updatedTask));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }

    @GetMapping("all")
    public ResponseEntity<List<TaskResponse>> findAll() {
        try {
            List<Task> tasks = taskService.findAll();
            List<TaskResponse> response = tasks.stream()
                    .map(taskMapper::toResponse)
                    .toList();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> findById(@PathVariable Long id) {
        try {
            Task task = taskService.findById(id);
            return ResponseEntity.ok(taskMapper.toResponse(task));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/completed/{id}")
    public ResponseEntity<TaskResponse> completed(@PathVariable Long id) {
        Task task = taskService.completedtask(id);
        return ResponseEntity.ok(taskMapper.toResponse(task));
    }

    @PutMapping("/start/{id}")
    public ResponseEntity<TaskResponse> started(@PathVariable Long id) {
        try {
            Task task = taskService.startTask(id);
            TaskResponse response = taskMapper.toResponse(task);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }

    @GetMapping("user/{id}")
    public ResponseEntity<List<TaskResponse>> findByUser(@PathVariable Long id) {
        try {
            List<Task> tasks = taskService.TaskByUser(id);
            List<TaskResponse> response = tasks.stream()
                    .map(taskMapper::toResponse)
                    .toList();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("project/{id}")
    public ResponseEntity<List<TaskResponse>> findByProject(@PathVariable Long id) {
        try {
            List<Task> tasks = taskService.TaskByUser(id);
            List<TaskResponse> response = tasks.stream()
                    .map(taskMapper::toResponse)
                    .toList();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
