package lv.dsns.support24.notificationlog.service;

import lv.dsns.support24.common.dto.response.PageResponse;
import lv.dsns.support24.notificationlog.controller.dto.request.NotificationLogRequestDTO;
import lv.dsns.support24.notificationlog.controller.dto.response.NotificationLogResponseDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface NotificationLogService {
    NotificationLogResponseDTO save(NotificationLogRequestDTO notificationLogRequestDTO);

    PageResponse<NotificationLogResponseDTO> findByNabatGroup(UUID nabatGroupId, Pageable pageable);

    @Transactional
    NotificationLogResponseDTO patch(UUID id, NotificationLogRequestDTO notificationLogRequestDTO);

    boolean existByEventId(UUID eventId);

    NotificationLogRequestDTO notificationLogRequestDTOBuilder(UUID notificationLogId,
                                                               UUID nabatGroupId,
                                                               UUID notifiedById,
                                                               String message);
}
