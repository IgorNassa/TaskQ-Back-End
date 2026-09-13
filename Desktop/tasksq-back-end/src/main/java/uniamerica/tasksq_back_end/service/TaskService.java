package uniamerica.tasksq_back_end.service;

import org.springframework.stereotype.Service;
import uniamerica.tasksq_back_end.entity.Task;
import uniamerica.tasksq_back_end.entity.enums.TaskStatus;
import uniamerica.tasksq_back_end.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;


    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    private Task saveTask(Task task){
        return taskRepository.save(task);
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow();
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public Task newTask(Task task){
        Long creatorId = 10L; /*id do criador da tarefa deve ser pego pelo usuario que esta logado*/
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        task.setCreatorId(creatorId);
        return saveTask(task);
    }

    public Task updateTask(Task task){
        task.setUpdatedAt(LocalDateTime.now());
        return saveTask(task);
    }

    public Task completedtask(Long id){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        LocalDateTime now = LocalDateTime.now();

        task.setStatus(TaskStatus.CONCLUIDO);
        task.setUpdatedAt(now);
        task.setCompletedAt(now);

        return saveTask(task);
    }

}
