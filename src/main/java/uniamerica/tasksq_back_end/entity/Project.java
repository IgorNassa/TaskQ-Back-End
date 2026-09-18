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
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_project")
public class Project extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private TaskPriority priority;

    @Column(name = "startDate", nullable = false)
    private LocalDate startDate;

    @Column(name = "deadLine", nullable = false)
    private LocalDate deadLine;

    @Column(name = "ownerId", nullable = false)
    private Long ownerId; /*depois relacionar com usuario responsavel pelo projeto*/

    @OneToMany(mappedBy = "project")
    private List<Task> tasks;
}