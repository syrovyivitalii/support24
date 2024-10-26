package lv.dsns.support24.notificationlog.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import lv.dsns.support24.common.entity.BaseEntity;
import lv.dsns.support24.nabatgroup.repository.entity.NabatGroup;
import lv.dsns.support24.user.repository.entity.SystemUsers;

import java.util.UUID;

@Entity
@Table(name = "tbl_notification_log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class NotificationLog extends BaseEntity {
    @Column(name = "nabat_group_id")
    private UUID nabatGroupId;

    @Column(name = "notification_id")
    private UUID notificationId;

    @Column(name = "message")
    private String message;

    @Column(name = "json_request")
    private String jsonRequest;

    @Column(name = "json_response")
    private String jsonResponse;

    @Column(name = "notified_by_id")
    private UUID notifiedByUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nabat_group_id", referencedColumnName = "id",nullable = false, insertable = false, updatable = false)
    private NabatGroup notificationLogGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notified_by_id", referencedColumnName = "id",nullable = false, insertable = false, updatable = false)
    private SystemUsers notificationLogUser;
}
