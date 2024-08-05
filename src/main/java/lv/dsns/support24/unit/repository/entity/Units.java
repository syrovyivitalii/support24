package lv.dsns.support24.unit.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import lv.dsns.support24.common.entity.BaseEntity;
import lv.dsns.support24.unit.controller.dto.enums.UnitType;
import lv.dsns.support24.user.repository.entity.SystemUsers;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.util.List;

@Entity
@Table(name = "tbl_units")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Units extends BaseEntity {

    @Column(name = "unit_name",nullable = false)
    private String unitName;

    @Column(name = "unit_type")
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private UnitType unitType;

    @OneToMany(mappedBy = "userUnit", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<SystemUsers> unitUser;
}
