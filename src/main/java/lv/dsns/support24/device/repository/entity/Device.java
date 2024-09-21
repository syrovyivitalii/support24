package lv.dsns.support24.device.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import lv.dsns.support24.common.entity.BaseEntity;
import lv.dsns.support24.device.controller.dto.enums.DeviceType;
import lv.dsns.support24.unit.repository.entity.Unit;
import org.checkerframework.common.aliasing.qual.Unique;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.util.UUID;

@Entity
@Table(name = "tbl_devices")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Device extends BaseEntity {
    @Column(name = "device_name")
    private String deviceName;

    @Column(name = "device_type")
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private DeviceType deviceType;

    @Column(name = "inventory_number")
    @Unique
    private String inventoryNumber;

    @Column(name = "decree_number")
    private String decreeNumber;

    @Column(name = "mac_adress")
    private String macAddress;

    @Column(name = "ip_adress")
    private String ipAddress;

    @Column(name = "production_year")
    private Integer productionYear;

    @Column(name = "note")
    private String note;

    @Column(name = "unit_id")
    private UUID unitId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", referencedColumnName = "id",nullable = false, insertable = false, updatable = false)
    private Unit deviceUnit;


}
