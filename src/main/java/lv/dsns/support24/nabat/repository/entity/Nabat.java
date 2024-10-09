package lv.dsns.support24.nabat.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import lv.dsns.support24.common.entity.BaseEntity;
import lv.dsns.support24.user.repository.entity.SystemUsers;

import java.util.HashSet;
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

    @ManyToMany
    @JoinTable(
            name = "user_nabat_group",
            joinColumns = @JoinColumn(name = "nabat_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<SystemUsers> users = new HashSet<>();


}
