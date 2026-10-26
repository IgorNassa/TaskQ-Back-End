package uniamerica.tasksq_back_end.dto.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import uniamerica.tasksq_back_end.dto.request.ProjectRequest;
import uniamerica.tasksq_back_end.dto.response.ProjectResponse;
import uniamerica.tasksq_back_end.entity.Project;
import uniamerica.tasksq_back_end.entity.enums.TaskPriority;
import uniamerica.tasksq_back_end.entity.enums.TaskStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ProjectMapperTest {

    private final ProjectMapper projectMapper =
            Mappers.getMapper(ProjectMapper.class);

    @Test
    void deveConverterRequestParaEntity() {
        ProjectRequest request = criarRequest();

        Project project = projectMapper.toEntity(request);

        assertNotNull(project);
        assertEquals(request.name(), project.getName());
        assertEquals(request.description(), project.getDescription());
        assertEquals(request.status(), project.getStatus());
        assertEquals(request.priority(), project.getPriority());
        assertEquals(request.startDate(), project.getStartDate());
        assertEquals(request.deadLine(), project.getDeadLine());
        assertEquals(request.ownerId(), project.getOwnerId());

        // Esses campos são ignorados pelo mapper.
        assertNull(project.getId());
        assertNull(project.getTasks());
    }

    @Test
    void deveAtualizarEntitySemAlterarCamposIgnorados() {
        ProjectRequest request = criarRequest();

        Project project = new Project();
        project.setId(99L);
        project.setName("Projeto antigo");
        project.setDescription("Descrição antiga");

        LocalDateTime createdAt =
                LocalDateTime.of(2026, 1, 10, 10, 0);
        LocalDateTime updatedAt =
                LocalDateTime.of(2026, 2, 10, 10, 0);

        project.setCreatedAt(createdAt);
        project.setUpdatedAt(updatedAt);
        project.setTasks(List.of());

        projectMapper.updateEntity(request, project);

        assertEquals(request.name(), project.getName());
        assertEquals(request.description(), project.getDescription());
        assertEquals(request.status(), project.getStatus());
        assertEquals(request.priority(), project.getPriority());
        assertEquals(request.startDate(), project.getStartDate());
        assertEquals(request.deadLine(), project.getDeadLine());
        assertEquals(request.ownerId(), project.getOwnerId());

        // O update não deve sobrescrever os campos ignorados.
        assertEquals(99L, project.getId());
        assertEquals(createdAt, project.getCreatedAt());
        assertEquals(updatedAt, project.getUpdatedAt());
        assertNotNull(project.getTasks());
        assertTrue(project.getTasks().isEmpty());
    }

    @Test
    void deveConverterEntityParaResponse() {
        Project project = criarProject();

        ProjectResponse response = projectMapper.toResponse(project);

        assertNotNull(response);
        assertEquals(project.getId(), response.id());
        assertEquals(project.getName(), response.name());
        assertEquals(project.getDescription(), response.description());
        assertEquals(project.getStatus(), response.status());
        assertEquals(project.getPriority(), response.priority());
        assertEquals(project.getStartDate(), response.startDate());
        assertEquals(project.getDeadLine(), response.deadLine());
        assertEquals(project.getOwnerId(), response.ownerId());
        assertEquals(project.getCreatedAt(), response.createdAt());
        assertEquals(project.getUpdatedAt(), response.updatedAt());
    }

    @Test
    void deveConverterListaDeProjectsParaResponses() {
        Project primeiro = criarProject();
        primeiro.setId(1L);
        primeiro.setName("Projeto A");

        Project segundo = criarProject();
        segundo.setId(2L);
        segundo.setName("Projeto B");

        List<ProjectResponse> responses =
                projectMapper.responseList(List.of(primeiro, segundo));

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("Projeto A", responses.get(0).name());
        assertEquals("Projeto B", responses.get(1).name());
    }

    @Test
    void deveRetornarListaVaziaQuandoReceberListaVazia() {
        List<ProjectResponse> responses =
                projectMapper.responseList(List.of());

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }

    private ProjectRequest criarRequest() {
        return new ProjectRequest(
                10L,
                "Task-Q",
                "Sistema de gerenciamento de tarefas",
                TaskStatus.values()[0],
                TaskPriority.values()[0],
                LocalDate.of(2026, 1, 1),
                LocalDate.now().plusDays(30),
                5L
        );
    }

    private Project criarProject() {
        Project project = new Project();

        project.setId(10L);
        project.setName("Task-Q");
        project.setDescription("Sistema de gerenciamento de tarefas");
        project.setStatus(TaskStatus.values()[0]);
        project.setPriority(TaskPriority.values()[0]);
        project.setStartDate(LocalDate.of(2026, 1, 1));
        project.setDeadLine(LocalDate.of(2026, 12, 31));
        project.setOwnerId(5L);
        project.setCreatedAt(LocalDateTime.of(2026, 1, 1, 10, 0));
        project.setUpdatedAt(LocalDateTime.of(2026, 1, 2, 10, 0));

        return project;
    }

    @Test
    void deveRetornarNullQuandoRequestForNull() {
        Project project = projectMapper.toEntity(null);

        assertNull(project);
    }

    @Test
    void naoDeveAtualizarEntityQuandoRequestForNull() {
        Project project = new Project();
        project.setId(99L);
        project.setName("Projeto original");

        projectMapper.updateEntity(null, project);

        assertEquals(99L, project.getId());
        assertEquals("Projeto original", project.getName());
    }

    @Test
    void deveRetornarNullQuandoProjectForNull() {
        ProjectResponse response = projectMapper.toResponse(null);

        assertNull(response);
    }

    @Test
    void deveRetornarNullQuandoListaForNull() {
        List<ProjectResponse> responses =
                projectMapper.responseList(null);

        assertNull(responses);
    }

}