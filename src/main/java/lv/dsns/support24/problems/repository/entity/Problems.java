package lv.dsns.support24.problems.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import lv.dsns.support24.common.entity.BaseEntity;
import lv.dsns.support24.task.repository.entity.Tasks;

import java.util.List;

@Entity
@Table(name = "tbl_common_problems")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Problems extends BaseEntity {

    @Column(name = "problem",nullable = false)
    private String problem;

    @OneToMany(mappedBy = "taskProblem", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<Tasks> problemType;

}
