package uniamerica.tasksq_back_end.dto.response;

import org.junit.jupiter.api.Test;
import uniamerica.tasksq_back_end.entity.enums.TaskPriority;
import uniamerica.tasksq_back_end.entity.enums.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProjectResponseTest {
    @Test
    void reponseTest(){
        ProjectResponse response = new ProjectResponse(
                1L,
                "Projeto teste",
                "Descricao do projeto",
                TaskStatus.PENDENTE,
                TaskPriority.ALTA,
                LocalDate.now(),
                LocalDate.now(),
                1L,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        assertEquals(1L, response.id());
        assertEquals("Projeto teste", response.name());
        assertEquals("Descricao do projeto", response.description());
    }
}
