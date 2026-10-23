package uniamerica.tasksq_back_end.dto.request;

import jakarta.validation.Validation;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validator;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProjectRequestTest {

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validRequest(){
        ProjectRequest projectRequest = new ProjectRequest(
                1L,
                "Projeto teste",
                "Descrição do projeto",
                uniamerica.tasksq_back_end.entity.enums.TaskStatus.PENDENTE,
                uniamerica.tasksq_back_end.entity.enums.TaskPriority.ALTA,
                LocalDate.now(),
                LocalDate.now(),
                1L
        );
        assertTrue(validator.validate(projectRequest).isEmpty());

    }
}
