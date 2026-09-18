package uniamerica.tasksq_back_end.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uniamerica.tasksq_back_end.audit.AuditableEntity;
import uniamerica.tasksq_back_end.entity.enums.TaskPriority;
import uniamerica.tasksq_back_end.entity.enums.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_task")
public class Task extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private TaskPriority priority;

    @ManyToOne
    @JoinColumn(name = "assignee_id", nullable = false)
    private User assigneeId;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creatorId;

    @Column(name = "deadLine", nullable = false)
    private LocalDate deadLine;

    @Column(name = "completedAt")
    private LocalDateTime completedAt;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}
