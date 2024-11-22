package lv.dsns.support24.nabat.repository;

import lv.dsns.support24.nabat.repository.entity.Nabat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NabatRepository extends JpaRepository<Nabat, UUID> {

    Page<Nabat> findByNabatGroupId(UUID nabatGroupId, Pageable pageable);

    boolean existsByUserIdAndNabatGroupId(UUID userId, UUID nabatGroupId);

    Optional<Nabat> findByUserIdAndNabatGroupId(UUID userId, UUID nabatGroupId);

    @Query("SELECT u.userId FROM Nabat u WHERE u.nabatGroupId = :nabatGroupId")
    List<UUID> findUserIdByNabatGroupId(@Param("nabatGroupId") UUID nabatGroupId);


    @Query(value = "SELECT tsu.sodu_id, MIN(p.phone) FROM tbl_phones p " +
            "JOIN tbl_system_users tsu ON tsu.id = p.user_id " +
            "WHERE p.user_id IN (:userIds) AND p.phone_type = 'Мобільний' " +
            "GROUP BY tsu.sodu_id", nativeQuery = true)
    List<Object[]> findPhonesByUserIds(@Param("userIds") List<UUID> userIds);


}
