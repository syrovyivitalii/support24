package lv.dsns.support24.problem.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import lv.dsns.support24.common.entity.BaseEntity;
import lv.dsns.support24.task.repository.entity.Task;

import java.util.List;

@Entity
@Table(name = "tbl_common_problems")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Problem extends BaseEntity {

    @Column(name = "problem",nullable = false)
    private String problem;

    @OneToMany(mappedBy = "taskProblem", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<Task> problemType;

}
