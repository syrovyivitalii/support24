package lv.dsns.support24.rank.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import lv.dsns.support24.common.entity.BaseEntity;
import lv.dsns.support24.user.repository.entity.SystemUsers;

import java.util.List;

@Entity
@Table(name = "tbl_ranks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Rank extends BaseEntity {
    @Column(name = "rank_name")
    private String rankName;

    @OneToMany(mappedBy = "userRank", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<SystemUsers> rankUser;
}
