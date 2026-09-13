package uniamerica.tasksq_back_end.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uniamerica.tasksq_back_end.entity.enums.TaskPriority;
import uniamerica.tasksq_back_end.entity.enums.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private Long assigneeId;
    private Long creatorId;
    private LocalDate deadLine;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;
}