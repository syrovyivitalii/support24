package lv.dsns.support24.position.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import lv.dsns.support24.common.entity.BaseEntity;
import lv.dsns.support24.user.repository.entity.SystemUsers;

import java.util.List;

@Entity
@Table(name = "tbl_positions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Position extends BaseEntity {

    @Column(name = "position_name", nullable = false, unique = true)
    private String positionName;


    @OneToMany(mappedBy = "userPosition", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<SystemUsers> positionUsers;
}
