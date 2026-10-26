package uniamerica.tasksq_back_end.dto.request;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RoleRequestTest {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validRequest(){
        RoleRequest roleRequest = new RoleRequest(
            "ADM"
        );
        assertTrue(validator.validate(roleRequest).isEmpty());
    }
}
