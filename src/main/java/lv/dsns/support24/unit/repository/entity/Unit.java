package lv.dsns.support24.unit.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import lv.dsns.support24.common.entity.BaseEntity;
import lv.dsns.support24.task.repository.entity.Task;
import lv.dsns.support24.unit.controller.dto.enums.UnitStatus;
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

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private UnitStatus unitStatus;

    @Column(name = "parent_unit_id")
    private UUID parentUnitId;

    @Column(name = "location")
    private String location;
    @Column(name = "street")
    private String street;

    @OneToMany(mappedBy = "userUnit", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<SystemUsers> unitUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_unit_id", referencedColumnName = "id",nullable = false, insertable = false, updatable = false)
    private Unit parentUnit;

}
