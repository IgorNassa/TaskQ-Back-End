package uniamerica.tasksq_back_end.dto.request;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import uniamerica.tasksq_back_end.entity.enums.UserStatus;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserUpdateRequestTest {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validRequest(){
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest(
                "João da Silva",
                "joao@email.com",
                "https://exemplo.com/avatar.png",
                1L,
                UserStatus.ATIVO
        );
        assertTrue(validator.validate(userUpdateRequest).isEmpty());
    }
}
