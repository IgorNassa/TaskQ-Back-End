package uniamerica.tasksq_back_end.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import uniamerica.tasksq_back_end.entity.Role;
import uniamerica.tasksq_back_end.repository.RoleRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RoleIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    RoleRepository rolerepository;

    @Test
    void createRoleTest() throws Exception{
        String request = """
                {
                "nome": "Gerente"
                }""";
        String response = mockMvc.perform(post("/api/cargos") /*simula requisicao*/
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

        assertTrue(rolerepository.existsById(id));

        Role saveRole = rolerepository.findById(id).orElseThrow();

        assertEquals("Gerente", saveRole.getNome());
    }
}
