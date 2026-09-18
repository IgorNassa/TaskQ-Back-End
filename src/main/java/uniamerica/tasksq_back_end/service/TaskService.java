package uniamerica.tasksq_back_end.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uniamerica.tasksq_back_end.entity.Task;
import uniamerica.tasksq_back_end.entity.enums.TaskPriority;
import uniamerica.tasksq_back_end.entity.enums.TaskStatus;
import uniamerica.tasksq_back_end.repository.TaskRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor

public class TaskService {
    private final TaskRepository taskRepository;

    private final ProjectService projectService;

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

    public Task newTask(Task task, Long projectId){
        Long creatorId = 10L; /*id do criador da tarefa deve ser pego pelo usuario que esta logado*/
        task.setProject(projectService.findById(projectId));
        task.setCreatorId(creatorId);
        return saveTask(task);
    }

    public Task updateTask(Task task){
        return saveTask(task);
    }

    public Task completedtask(Long id){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
        task.setStatus(TaskStatus.CONCLUIDO);
        task.setCompletedAt(LocalDateTime.now());

        return saveTask(task);
    }

    public Task startTask(Long id){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
        task.setStatus(TaskStatus.ANDAMENTO);
        return saveTask(task);
    }

    public void checkStatus(List<Task> tasks){
        for (Task task : tasks) {
            if (task.getDeadLine().isBefore(LocalDate.now()) && task.getStatus() != TaskStatus.CONCLUIDO) {
                task.setStatus(TaskStatus.ATRASADA);
                taskRepository.save(task);
            }
        }
    }

    @PostConstruct
    public void loadService(){
        checkStatus(findAll());
    }
}
