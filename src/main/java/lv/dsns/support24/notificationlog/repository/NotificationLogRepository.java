package lv.dsns.support24.notificationlog.repository;

import lv.dsns.support24.common.dto.response.PageResponse;
import lv.dsns.support24.notificationlog.repository.entity.NotificationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, UUID> {
    Page<NotificationLog> findByNabatGroupId(UUID nabatGroupId, Pageable pageable);

    boolean existsByEventId(UUID eventId);
}
