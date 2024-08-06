package lv.dsns.support24.task.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import lv.dsns.support24.common.entity.BaseEntity;
import lv.dsns.support24.problems.repository.entity.Problems;
import lv.dsns.support24.task.controller.dto.enums.Priority;
import lv.dsns.support24.task.controller.dto.enums.Status;
import lv.dsns.support24.unit.repository.entity.Units;
import lv.dsns.support24.user.repository.entity.SystemUsers;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tbl_tasks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Tasks extends BaseEntity {

    @Column(name = "description",nullable = false)
    private String description;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "completed_date")
    private LocalDateTime completedDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private Status status;

    @Column(name = "priority")
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private Priority priority;

    @Column(name = "assigned_for_id")
    private UUID assignedForId;

    @Column(name = "assigned_by_id")
    private UUID assignedById;

    @Column(name = "created_by_id")
    private UUID createdById;

    @Column(name = "problem_type_id")
    private UUID problemTypeId;

    private boolean notified;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_for_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private SystemUsers assignedFor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_by_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private SystemUsers assignedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id", referencedColumnName = "id",nullable = false, insertable = false, updatable = false)
    private SystemUsers createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_type_id", referencedColumnName = "id",nullable = false, insertable = false, updatable = false)
    private Problems taskProblem;

    @PrePersist
    protected void onCreate() {
        if (status == null) {
            status = Status.UNCOMPLETED;
        }
        if (priority == null) {
            priority = Priority.LOW;
        }
    }
}
