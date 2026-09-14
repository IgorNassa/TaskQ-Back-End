package uniamerica.tasksq_back_end.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uniamerica.tasksq_back_end.entity.enums.TaskPriority;
import uniamerica.tasksq_back_end.entity.enums.TaskStatus;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {
    private Long id;

    @NotBlank
    @Size(min = 3, max = 100)
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private TaskStatus status;

    @NotNull
    private TaskPriority priority;

    @NotNull
    private Long assigneeId;

    @NotNull
    @FutureOrPresent
    private LocalDate deadLine;
}