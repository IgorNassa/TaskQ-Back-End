package uniamerica.tasksq_back_end.dto.request;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordUpdateRequestTest {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validRequest(){
        PasswordUpdateRequest passwordUpdateRequest = new PasswordUpdateRequest(
                "123456789"
        );
        assertTrue(validator.validate(passwordUpdateRequest).isEmpty());
    }
}
