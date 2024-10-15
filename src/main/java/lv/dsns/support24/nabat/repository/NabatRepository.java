package lv.dsns.support24.nabat.repository;

import lv.dsns.support24.nabat.repository.entity.Nabat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NabatRepository extends JpaRepository<Nabat, UUID> {
    List<Nabat> findByNabatGroupId(UUID nabatGroupId);

    boolean existsByUserIdAndNabatGroupId(UUID userId, UUID nabatGroupId);
}
