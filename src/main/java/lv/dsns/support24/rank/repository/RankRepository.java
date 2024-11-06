package lv.dsns.support24.rank.repository;

import lv.dsns.support24.rank.repository.entity.Rank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RankRepository extends JpaRepository<Rank, UUID> {

    boolean existsByRankName(String name);
}
