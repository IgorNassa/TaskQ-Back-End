package uniamerica.tasksq_back_end.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import uniamerica.tasksq_back_end.entity.Task;
import uniamerica.tasksq_back_end.dto.request.TaskRequest;
import uniamerica.tasksq_back_end.dto.mapper.TaskMapper;
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

    private final UserService userService;
    private final XpService xpService;
    private final TaskMapper taskMapper;

    private Task saveTask(Task task){
        if (task.getStatus() == TaskStatus.CONCLUIDO && task.getCompletedAt() == null) {
            task.setCompletedAt(LocalDateTime.now());
        }
        Task savedTask = taskRepository.save(task);
        if (savedTask.getStatus() == TaskStatus.CONCLUIDO) {
            xpService.rewardCompletedTask(savedTask.getId());
        }
        return savedTask;
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada"));
    }

    @Transactional
    public void deleteTask(Long id) {
        taskRepository.delete(findById(id));
    }

    @Transactional
    public Task newTask(Task task, Long projectId, Long userId, Long creatorId){
        task.setAssigneeId(userService.findUser(userId));
        task.setProject(projectService.findById(projectId));
        // Até existir autenticação, o criador pode ser informado; o padrão é o proprietário do projeto.
        task.setCreatorId(userService.findUser(creatorId != null ? creatorId : task.getProject().getOwnerId()));
        return saveTask(task);
    }

    @Transactional
    public Task updateTask(Task task){
        return saveTask(task);
    }

    @Transactional
    public Task updateTask(TaskRequest request) {
        if (request.id() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o ID da tarefa para atualizar");
        }
        Task task = taskRepository.findByIdForUpdate(request.id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada"));
        taskMapper.updateEntity(request, task);
        task.setAssigneeId(userService.findUser(request.assigneeId()));
        task.setProject(projectService.findById(request.projectId()));
        if (task.getStatus() != TaskStatus.CONCLUIDO) {
            task.setCompletedAt(null);
        }
        return saveTask(task);
    }

    @Transactional
    public Task completeTask(Long id){
        Task task = taskRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada"));
        if (task.getStatus() != TaskStatus.CONCLUIDO) {
            task.setStatus(TaskStatus.CONCLUIDO);
            task.setCompletedAt(LocalDateTime.now());
        }
        return saveTask(task);
    }

    @Transactional
    public Task startTask(Long id){
        Task task = taskRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada"));
        task.setStatus(TaskStatus.ANDAMENTO);
        task.setCompletedAt(null);
        return saveTask(task);
    }

    public void checkStatus(List<Task> tasks){
        for (Task task : tasks) {
            if (task.getDeadLine() != null && task.getDeadLine().isBefore(LocalDate.now()) && task.getStatus() != TaskStatus.CONCLUIDO) {
                task.setStatus(TaskStatus.ATRASADA);
                taskRepository.save(task);
            }
        }
    }

    public List<Task> findByUser(Long id) {
        userService.findUser(id);
        return taskRepository.findByAssigneeId_Id(id);
    }

    public List<Task> findByProject(Long id) {
        projectService.findById(id);
        return taskRepository.findByProjectId(id);
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
