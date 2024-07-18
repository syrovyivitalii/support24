package lv.dsns.support24.task.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import lv.dsns.support24.common.entity.BaseEntity;
import lv.dsns.support24.problems.repository.entity.CommonProblems;
import lv.dsns.support24.task.controller.dto.enums.Priority;
import lv.dsns.support24.task.controller.dto.enums.Status;
import lv.dsns.support24.user.repository.entity.SystemUsers;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.hibernate.type.SqlTypes;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "description",nullable = false)
    private String description;

    @Column(name = "due_date")
    private LocalDate dueDate;

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

    @Column(name = "problem_type")
    private UUID problemTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_for_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private SystemUsers assignedFor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_by_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private SystemUsers assignedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id", referencedColumnName = "id",nullable = false, insertable = false, updatable = false)
    private SystemUsers createdBy;

    @OneToMany(mappedBy = "problemType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommonProblems> taskProblem;

    @PrePersist
    protected void onCreate() {
        if (status == null) {
            status = Status.UNCOMPLETED;
        }
    }
}
