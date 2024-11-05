package lv.dsns.support24.notificationlog.service;

import jakarta.servlet.http.HttpServletResponse;
import lv.dsns.support24.common.dto.response.PageResponse;
import lv.dsns.support24.notificationlog.controller.dto.request.NotificationLogRequestDTO;
import lv.dsns.support24.notificationlog.controller.dto.response.NotificationLogResponseDTO;
import lv.dsns.support24.notificationlog.controller.dto.response.NotifiedUsersResponseDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.Writer;
import java.util.UUID;

public interface NotificationLogService {
    NotificationLogResponseDTO save(NotificationLogRequestDTO notificationLogRequestDTO);

    PageResponse<NotificationLogResponseDTO> findByNabatGroup(UUID nabatGroupId, Pageable pageable);

    @Transactional
    NotificationLogResponseDTO patch(UUID id, NotificationLogRequestDTO notificationLogRequestDTO);


    NotifiedUsersResponseDTO getNotificationInfo(UUID eventId);

    void getNotifyInfoToCsv(HttpServletResponse response, UUID eventId) throws IOException;

    NotificationLogRequestDTO notificationLogRequestDTOBuilder(UUID notificationLogId,
                                                               UUID nabatGroupId,
                                                               UUID notifiedById,
                                                               String message);

    NotificationLogRequestDTO notificationLogRequestDTOBuilder(String jsonResponse);
}
