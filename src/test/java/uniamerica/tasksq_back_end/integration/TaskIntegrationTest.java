package uniamerica.tasksq_back_end.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import uniamerica.tasksq_back_end.entity.Task;
import uniamerica.tasksq_back_end.repository.TaskRepository;
import uniamerica.tasksq_back_end.service.ProjectService;
import uniamerica.tasksq_back_end.service.RoleService;
import uniamerica.tasksq_back_end.service.UserService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc

public class TaskIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    ProjectService projectService;

    @Test
    void createTasksTest() throws Exception{
        String request = """
            {
                "title": "Tarefa de Teste",
                "description": "teste",
                "status": "PENDENTE",
                "priority": "ALTA",
                "assigneeId": 1,
                "deadLine": "%s",
                "projectId": 1,
                "creatorId": 1
            }
            """
            .formatted(LocalDate.now().plusDays(10)
        );
        String response = mockMvc.perform(post("/api/task/create") /*simula requisicao*/
                        .contentType(MediaType.APPLICATION_JSON) /*resultado vai ser json*/
                        .content(request)) /*envia json do request*/
                .andExpect(status().isCreated()) /*Verifica a resposta http*/
                .andExpect(content().contentTypeCompatibleWith( /*Verifica se a resposta é json*/
                        MediaType.APPLICATION_JSON
                ))
                .andReturn() /*captura o resultaddo*/
                .getResponse() /*acessa a resposta HTTP*/
                .getContentAsString();  /*pega a resposta com texto*/
        JsonNode json = objectMapper.readTree(response); /*transforma json em java*/
        Long id = json.get("id").asLong();
        assertTrue(taskRepository.existsById(id));

        Task saveTask = taskRepository.findById(id).orElseThrow();

        assertEquals("Tarefa de Teste", saveTask.getTitle());
        assertEquals("teste", saveTask.getDescription());
    }



}
