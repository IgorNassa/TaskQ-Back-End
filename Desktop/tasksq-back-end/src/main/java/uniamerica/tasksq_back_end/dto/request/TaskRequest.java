package uniamerica.tasksq_back_end.dto.request;

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
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private Long assigneeId;
    private LocalDate deadLine;
}