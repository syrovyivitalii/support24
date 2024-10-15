package lv.dsns.support24.nabat.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import lv.dsns.support24.common.entity.BaseEntity;
import lv.dsns.support24.nabatgroup.repository.entity.NabatGroup;
import lv.dsns.support24.unit.repository.entity.Unit;
import lv.dsns.support24.user.repository.entity.SystemUsers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tbl_nabat")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Nabat extends BaseEntity {
    @Column(name = "nabat_group_id")
    private UUID nabatGroupId;

    @Column(name = "user_id")
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nabat_group_id", referencedColumnName = "id",nullable = false, insertable = false, updatable = false)
    private NabatGroup nabatGroup;

//    @OneToMany(mappedBy = "userNabat", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
//    private List<SystemUsers> nabatUsers ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id",nullable = false, insertable = false, updatable = false)
    private SystemUsers nabatUsers;

}
