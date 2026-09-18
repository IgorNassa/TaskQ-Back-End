package uniamerica.tasksq_back_end.dto.response;

import uniamerica.tasksq_back_end.entity.enums.TaskPriority;
import uniamerica.tasksq_back_end.entity.enums.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ProjectResponse(
        Long id,
        String name,
        String description,
        TaskStatus status,
        TaskPriority priority,
        LocalDate startDate,
        LocalDate deadLine,
        Long ownerId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
