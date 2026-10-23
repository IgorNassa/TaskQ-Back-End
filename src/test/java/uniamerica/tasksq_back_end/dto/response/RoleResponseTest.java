package uniamerica.tasksq_back_end.dto.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoleResponseTest {
    @Test
    void reponseTest(){
        RoleResponse response = new RoleResponse(
                1L,
                "ADM"
        );

        assertEquals(1L, response.id());
        assertEquals("ADM", response.nome());
    }
}
