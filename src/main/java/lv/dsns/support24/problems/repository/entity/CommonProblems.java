package lv.dsns.support24.problems.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import lv.dsns.support24.common.entity.BaseEntity;
import lv.dsns.support24.task.repository.entity.Tasks;

@Entity
@Table(name = "tbl_common_problems")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class CommonProblems extends BaseEntity {

    @Column(name = "problem",nullable = false)
    private String problem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "problem_type",nullable = false, insertable = false, updatable = false)
    private Tasks problemType;

}
