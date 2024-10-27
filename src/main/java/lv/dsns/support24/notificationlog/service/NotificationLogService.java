package lv.dsns.support24.notificationlog.service;

import lv.dsns.support24.common.dto.response.PageResponse;
import lv.dsns.support24.notificationlog.controller.dto.request.NotificationLogRequestDTO;
import lv.dsns.support24.notificationlog.controller.dto.response.NotificationLogResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface NotificationLogService {
    NotificationLogResponseDTO save(NotificationLogRequestDTO notificationLogRequestDTO);

    PageResponse<NotificationLogResponseDTO> findByNabatGroup(UUID nabatGroupId, Pageable pageable);

    NotificationLogRequestDTO notificationLogRequestDTOBuilder(UUID notificationLogId,
                                                               UUID nabatGroupId,
                                                               UUID notifiedById,
                                                               String message);
}
