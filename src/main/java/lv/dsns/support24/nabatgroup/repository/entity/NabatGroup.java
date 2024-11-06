package lv.dsns.support24.nabatgroup.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import lv.dsns.support24.common.entity.BaseEntity;
import lv.dsns.support24.nabat.repository.entity.Nabat;
import lv.dsns.support24.notificationlog.repository.entity.NotificationLog;
import lv.dsns.support24.unit.repository.entity.Unit;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tbl_nabat_groups")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class NabatGroup extends BaseEntity {
    @Column(name = "unit_id")
    private UUID unitId;

    @Column(name = "group_name")
    private String groupName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", referencedColumnName = "id",nullable = false, insertable = false, updatable = false)
    private Unit nabatGroupUnit;

    @OneToMany(mappedBy = "nabatGroup", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.REMOVE})
    private List<Nabat> groupNabats ;

    @OneToMany(mappedBy = "notificationLogGroup", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.REMOVE})
    private List<NotificationLog> groupNotificationLogs;
}
