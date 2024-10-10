package lv.dsns.support24.position.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lv.dsns.support24.common.entity.BaseEntity;

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
}
