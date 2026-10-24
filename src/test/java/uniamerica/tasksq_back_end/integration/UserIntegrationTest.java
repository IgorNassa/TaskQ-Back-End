package uniamerica.tasksq_back_end.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import uniamerica.tasksq_back_end.dto.request.RoleRequest;
import uniamerica.tasksq_back_end.entity.User;
import uniamerica.tasksq_back_end.repository.UserRepository;
import uniamerica.tasksq_back_end.service.RoleService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    RoleService roleService;
    @Autowired
    UserRepository userRepository;

    @Test
    void createUserTest() throws Exception{
        RoleRequest roleRequest = new RoleRequest(
                "ADM"
        );
        roleService.save(roleRequest);
        String request = """
            {
                "nome": "teste",
                "email": "testeintegracao@gmail.com",
                "urlAvatar": null,
                "senha": "teste123",
                "cargoId": 1,
                "status": "ATIVO"
            }""";
        String response = mockMvc.perform(post("/api/usuarios") /*simula requisicao*/
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

        assertTrue(userRepository.existsById(id));

        User saveUser = userRepository.findById(id).orElseThrow();

        assertEquals("teste", saveUser.getNome());
        assertEquals("testeintegracao@gmail.com", saveUser.getEmail());
    }
}
