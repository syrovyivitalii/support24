package lv.dsns.support24.phone.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import lv.dsns.support24.common.entity.BaseEntity;
import lv.dsns.support24.phone.controller.dto.enums.PhoneStatus;
import lv.dsns.support24.phone.controller.dto.enums.PhoneType;
import lv.dsns.support24.user.repository.entity.SystemUsers;

import java.util.UUID;

@Entity
@Table(name = "tbl_phones")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Phone extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "phone_type", nullable = false)
    private PhoneType phoneType;

    @Column(name = "status", nullable = false)
    private PhoneStatus phoneStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id",nullable = false, insertable = false, updatable = false)
    private SystemUsers phoneUser;

}
