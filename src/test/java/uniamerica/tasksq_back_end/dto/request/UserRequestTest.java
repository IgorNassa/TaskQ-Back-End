package uniamerica.tasksq_back_end.dto.request;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import uniamerica.tasksq_back_end.entity.enums.UserStatus;


import static org.junit.jupiter.api.Assertions.assertTrue;

class UserRequestTest {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validRequest(){
        UserRequest userRequest = new UserRequest(
                "João da Silva",
                "joao@email.com",
                "https://exemplo.com/avatar.png",
                "Senha123",
                1L,
                UserStatus.ATIVO
        );
        assertTrue(validator.validate(userRequest).isEmpty());
    }
}
