package uniamerica.tasksq_back_end.dto.response;

import org.junit.jupiter.api.Test;
import uniamerica.tasksq_back_end.entity.enums.TaskPriority;
import uniamerica.tasksq_back_end.entity.enums.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskResponseTest {
    @Test
    void reponseTest(){
        TaskResponse response = new TaskResponse(
                1L,
                "Tarefa teste",
                "Descricao da tarefa",
                TaskStatus.PENDENTE,
                TaskPriority.ALTA,
                1L,
                1L,
                LocalDate.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        );

        assertEquals(1L, response.id());
        assertEquals("Tarefa teste", response.title());
        assertEquals("Descricao da tarefa", response.description());
    }
}
