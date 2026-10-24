package uniamerica.tasksq_back_end.integration;

import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import uniamerica.tasksq_back_end.controller.UserController;
import uniamerica.tasksq_back_end.dto.request.RoleRequest;
import uniamerica.tasksq_back_end.dto.request.UserRequest;
import uniamerica.tasksq_back_end.entity.Project;
import uniamerica.tasksq_back_end.entity.enums.UserStatus;
import uniamerica.tasksq_back_end.repository.ProjectRepository;
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
public class ProjectIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Test
    void createProjectTest() throws Exception {
        RoleRequest roleRequest = new RoleRequest(
                "ADM"
        );
        roleService.save(roleRequest);
        UserRequest userRequest = new UserRequest(
                "Teste",
                "teste@teste.com",
                null,
                "teste123",
                1L,
                UserStatus.ATIVO
        );
        userService.save(userRequest);
        String request = """
                {
                    "name": "Projeto de integracao",
                    "description": "Teste",
                    "status": "PENDENTE",
                    "priority": "ALTA",
                    "startDate": "%s",
                    "deadLine": "%s",
                    "ownerId": 1
                }
                """.formatted(
                LocalDate.now(),
                LocalDate.now().plusDays(10)
        );

        String response = mockMvc.perform(post("/api/project/create") /*simula requisicao*/
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

        assertTrue(projectRepository.existsById(id));

        Project savedProject = projectRepository.findById(id)
                .orElseThrow();

        assertEquals("Projeto de integracao", savedProject.getName());
        assertEquals(
                "Teste",
                savedProject.getDescription()
        );
    }
}
