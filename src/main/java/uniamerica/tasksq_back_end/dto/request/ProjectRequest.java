package uniamerica.tasksq_back_end.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import uniamerica.tasksq_back_end.entity.enums.TaskPriority;
import uniamerica.tasksq_back_end.entity.enums.TaskStatus;

import java.time.LocalDate;

public record ProjectRequest(
        Long id,

        @NotBlank
        @Size(min = 3, max = 100)
        String name,

        @NotBlank
        String description,

        @NotNull
        TaskStatus status,

        @NotNull
        TaskPriority priority,

        @NotNull
        @FutureOrPresent
        LocalDate startDate,

        @NotNull
        @FutureOrPresent
        LocalDate deadLine,

        @NotNull
        Long ownerId
) {
}