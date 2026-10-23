package uniamerica.tasksq_back_end.dto.request;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import uniamerica.tasksq_back_end.entity.enums.TaskPriority;
import uniamerica.tasksq_back_end.entity.enums.TaskStatus;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskRequestTest {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validRequest(){
        TaskRequest taskRequest = new TaskRequest(
            1L,
            "Tarefa teste",
            "Descrição da tarefa",
            TaskStatus.PENDENTE,
            TaskPriority.ALTA,
            1L,
            LocalDate.now(),
            1L,
            1L
        );
        assertTrue(validator.validate(taskRequest).isEmpty());
    }
}
