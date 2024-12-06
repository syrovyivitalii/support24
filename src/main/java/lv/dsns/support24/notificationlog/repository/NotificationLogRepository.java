package lv.dsns.support24.notificationlog.repository;

import lv.dsns.support24.notificationlog.repository.entity.NotificationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, UUID> {
    Page<NotificationLog> findByNabatGroupId(UUID nabatGroupId, Pageable pageable);

    Optional<NotificationLog> findByEventId(UUID eventId);

    @Query("""
        SELECT nl
        FROM NotificationLog nl
        WHERE nl.nabatGroupId = :nabatGroupId
        ORDER BY nl.createdDate DESC
        LIMIT 1
        """)
    Optional<NotificationLog> findTopByNabatGroupIdOrderByCreatedDateDesc(@Param("nabatGroupId") UUID nabatGroupId);


}
