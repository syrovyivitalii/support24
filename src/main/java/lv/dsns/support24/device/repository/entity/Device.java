package lv.dsns.support24.device.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import lv.dsns.support24.common.entity.BaseEntity;
import lv.dsns.support24.device.controller.dto.enums.DeviceStatus;
import lv.dsns.support24.device.controller.dto.enums.DeviceType;
import lv.dsns.support24.unit.repository.entity.Unit;
import lv.dsns.support24.user.repository.entity.SystemUsers;
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

    @Column(name = "specifications")
    private String specifications;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private DeviceStatus deviceStatus;

    @Column(name = "unit_id")
    private UUID unitId;

    @Column(name = "user_id")
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", referencedColumnName = "id",nullable = false, insertable = false, updatable = false)
    private Unit deviceUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id",nullable = false, insertable = false, updatable = false)
    private SystemUsers deviceUser;

    @PrePersist
    protected void onCreate() {
        if (deviceStatus == null) {
            deviceStatus = DeviceStatus.ACTIVE;
        }
    }

}
