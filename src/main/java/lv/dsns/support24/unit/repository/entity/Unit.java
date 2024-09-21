package lv.dsns.support24.unit.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import lv.dsns.support24.common.entity.BaseEntity;
import lv.dsns.support24.unit.controller.dto.enums.UnitType;
import lv.dsns.support24.user.repository.entity.SystemUsers;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tbl_units")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Unit extends BaseEntity {

    @Column(name = "unit_name",nullable = false)
    private String unitName;

    @Column(name = "unit_type")
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private UnitType unitType;

    @Column(name = "parent_unit_id", insertable = false, updatable = false)
    private UUID parentUnitId;

    @Column(name = "group_id")
    private int groupId;

    @OneToMany(mappedBy = "userUnit", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<SystemUsers> unitUser;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_unit_id", referencedColumnName = "id")
    private Unit parentUnit;

}
