package uniamerica.tasksq_back_end.dto.response;

import org.junit.jupiter.api.Test;
import uniamerica.tasksq_back_end.entity.enums.UserRank;
import uniamerica.tasksq_back_end.entity.enums.UserStatus;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserResponseTest {
    @Test
    void reponseTest(){
        UserResponse response = new UserResponse(
                1L,
                "Pedro",
                "pedro@email.com",
                "exemplo.com/avatar.png",
                new RoleResponse(1L, "ADMIN"),
                UserStatus.ATIVO,
                100L,
                UserRank.INICIANTE,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        assertEquals(1L, response.id());
        assertEquals("Pedro", response.nome());
        assertEquals("pedro@email.com", response.email());
    }
}
