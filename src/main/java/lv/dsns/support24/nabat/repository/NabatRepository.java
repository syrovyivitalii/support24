package lv.dsns.support24.nabat.repository;

import lv.dsns.support24.nabat.repository.entity.Nabat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NabatRepository extends JpaRepository<Nabat, UUID> {
    Page<Nabat> findByNabatGroupId(UUID nabatGroupId, Pageable pageable);

    boolean existsByUserIdAndNabatGroupId(UUID userId, UUID nabatGroupId);

    Optional<Nabat> findByUserIdAndNabatGroupId(UUID userId, UUID nabatGroupId);


}
