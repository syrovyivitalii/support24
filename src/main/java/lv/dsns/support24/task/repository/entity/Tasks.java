package lv.dsns.support24.task.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import lv.dsns.support24.common.entity.BaseEntity;
import lv.dsns.support24.user.repository.entity.SystemUsers;

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

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "status",nullable = false)
    private String status;

    @Column(name = "priority",nullable = false)
    private String priority;

    @Column(name = "created_for_id")
    private UUID createdForId;

    @Column(name = "create_by_id")
    private UUID createdById;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_for_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private SystemUsers createdFor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_by_id", referencedColumnName = "id",nullable = false, insertable = false, updatable = false)
    private SystemUsers createdBy;
}
