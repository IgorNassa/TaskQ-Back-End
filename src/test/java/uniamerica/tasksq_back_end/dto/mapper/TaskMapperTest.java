package uniamerica.tasksq_back_end.dto.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import uniamerica.tasksq_back_end.dto.request.TaskRequest;
import uniamerica.tasksq_back_end.dto.response.TaskResponse;
import uniamerica.tasksq_back_end.entity.Task;
import uniamerica.tasksq_back_end.entity.User;
import uniamerica.tasksq_back_end.entity.enums.TaskPriority;
import uniamerica.tasksq_back_end.entity.enums.TaskStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TaskMapperTest {

    private final TaskMapper taskMapper =
            Mappers.getMapper(TaskMapper.class);

    @Test
    void deveConverterRequestParaEntity() {
        TaskRequest request = criarRequest();

        Task task = taskMapper.toEntity(request);

        assertNotNull(task);
        assertEquals(request.title(), task.getTitle());
        assertEquals(request.description(), task.getDescription());
        assertEquals(request.status(), task.getStatus());
        assertEquals(request.priority(), task.getPriority());
        assertEquals(request.deadLine(), task.getDeadLine());

        assertNull(task.getId());
        assertNull(task.getProject());
        assertNull(task.getAssigneeId());
        assertNull(task.getCreatorId());
        assertNull(task.getCompletedAt());
    }

    @Test
    void deveAtualizarEntitySemAlterarCamposIgnorados() {
        TaskRequest request = criarRequest();

        Task task = new Task();
        task.setId(99L);
        task.setTitle("Tarefa antiga");
        task.setDescription("Descrição antiga");

        User responsavel = criarUsuario(10L);
        User criador = criarUsuario(20L);

        task.setAssigneeId(responsavel);
        task.setCreatorId(criador);
        task.setCompletedAt(LocalDateTime.of(2026, 1, 15, 10, 0));

        LocalDateTime createdAt =
                LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime updatedAt =
                LocalDateTime.of(2026, 1, 2, 10, 0);

        task.setCreatedAt(createdAt);
        task.setUpdatedAt(updatedAt);

        taskMapper.updateEntity(request, task);

        assertEquals(request.title(), task.getTitle());
        assertEquals(request.description(), task.getDescription());
        assertEquals(request.status(), task.getStatus());
        assertEquals(request.priority(), task.getPriority());
        assertEquals(request.deadLine(), task.getDeadLine());

        assertEquals(99L, task.getId());
        assertSame(responsavel, task.getAssigneeId());
        assertSame(criador, task.getCreatorId());
        assertEquals(createdAt, task.getCreatedAt());
        assertEquals(updatedAt, task.getUpdatedAt());
        assertEquals(
                LocalDateTime.of(2026, 1, 15, 10, 0),
                task.getCompletedAt()
        );
    }

    @Test
    void deveConverterEntityParaResponse() {
        Task task = new Task();

        task.setId(1L);
        task.setTitle("Implementar testes");
        task.setDescription("Criar testes unitários para o mapper");
        task.setStatus(TaskStatus.values()[0]);
        task.setPriority(TaskPriority.values()[0]);
        task.setAssigneeId(criarUsuario(10L));
        task.setCreatorId(criarUsuario(20L));
        task.setDeadLine(LocalDate.of(2026, 12, 31));
        task.setCreatedAt(LocalDateTime.of(2026, 1, 1, 10, 0));
        task.setUpdatedAt(LocalDateTime.of(2026, 1, 2, 10, 0));
        task.setCompletedAt(null);

        TaskResponse response = taskMapper.toResponse(task);

        assertNotNull(response);
        assertEquals(task.getId(), response.id());
        assertEquals(task.getTitle(), response.title());
        assertEquals(task.getDescription(), response.description());
        assertEquals(task.getStatus(), response.status());
        assertEquals(task.getPriority(), response.priority());
        assertEquals(10L, response.assigneeId());
        assertEquals(20L, response.creatorId());
        assertEquals(task.getDeadLine(), response.deadLine());
        assertEquals(task.getCreatedAt(), response.createdAt());
        assertEquals(task.getUpdatedAt(), response.updatedAt());
        assertNull(response.completedAt());
    }

    private TaskRequest criarRequest() {
        return new TaskRequest(
                1L,
                "Implementar testes",
                "Criar testes unitários para o mapper",
                TaskStatus.values()[0],
                TaskPriority.values()[0],
                10L,
                LocalDate.now().plusDays(30),
                5L,
                20L
        );
    }

    private User criarUsuario(Long id) {
        User user = new User();
        user.setId(id);
        return user;
    }
}