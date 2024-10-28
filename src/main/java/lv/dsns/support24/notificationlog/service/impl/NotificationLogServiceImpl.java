package lv.dsns.support24.notificationlog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lv.dsns.support24.common.dto.response.PageResponse;
import lv.dsns.support24.common.exception.ClientBackendException;
import lv.dsns.support24.common.exception.ErrorCode;
import lv.dsns.support24.notificationlog.controller.dto.request.NotificationLogRequestDTO;
import lv.dsns.support24.notificationlog.controller.dto.response.NotificationLogResponseDTO;
import lv.dsns.support24.notificationlog.mapper.NotificationLogMapper;
import lv.dsns.support24.notificationlog.repository.NotificationLogRepository;
import lv.dsns.support24.notificationlog.repository.entity.NotificationLog;
import lv.dsns.support24.notificationlog.service.NotificationLogService;
import lv.dsns.support24.notifyresult.dto.NotifyResultResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationLogServiceImpl implements NotificationLogService {

    private final NotificationLogMapper notificationLogMapper;
    private final NotificationLogRepository notificationLogRepository;


    @Override
    @Transactional
    public NotificationLogResponseDTO save(NotificationLogRequestDTO notificationLogRequestDTO) {
        var notificationLog = notificationLogMapper.mapToEntity(notificationLogRequestDTO);

        var savedNotificationLog = notificationLogRepository.save(notificationLog);

        return notificationLogMapper.mapToDTO(savedNotificationLog);
    }

    @Override
    public PageResponse<NotificationLogResponseDTO> findByNabatGroup(UUID nabatGroupId, Pageable pageable) {
        Page<NotificationLog> byNabatGroupId = notificationLogRepository.findByNabatGroupId(nabatGroupId, pageable);

        List<NotificationLogResponseDTO> notificationLogDTOs = byNabatGroupId.stream()
                .map(notificationLogMapper::mapToDTO)
                .collect(Collectors.toList());

        return PageResponse.<NotificationLogResponseDTO>builder()
                .totalPages((long) byNabatGroupId.getTotalPages())
                .pageSize((long) pageable.getPageSize())
                .totalElements(byNabatGroupId.getTotalElements())
                .content(notificationLogDTOs)
                .build();

    }

    @Transactional
    @Override
    public NotificationLogResponseDTO patch(UUID id, NotificationLogRequestDTO notificationLogRequestDTO) {
        var notificationLog = notificationLogRepository.findById(id).orElseThrow(
                () -> new ClientBackendException(ErrorCode.NOTIFICATION_LOG_NOT_FOUND));

        notificationLogMapper.patchMerge(notificationLogRequestDTO, notificationLog);

        return notificationLogMapper.mapToDTO(notificationLog);
    }

    @Override
    public NotifyResultResponseDTO getNotificationInfo(UUID eventId) {
        NotificationLog notificationLog = notificationLogRepository.findByEventId(eventId).orElseThrow(() -> new ClientBackendException(ErrorCode.NOTIFICATION_LOG_NOT_FOUND));

        String jsonResponse = notificationLog.getJsonResponse();

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(jsonResponse, NotifyResultResponseDTO.class);
        } catch (Exception e) {
            throw new ClientBackendException(ErrorCode.UNKNOWN_SERVER_ERROR);
        }
    }


    @Override
    public NotificationLogRequestDTO notificationLogRequestDTOBuilder(UUID eventId,
                                                                      UUID nabatGroupId,
                                                                      UUID notifiedById,
                                                                      String message) {
        return NotificationLogRequestDTO.builder()
                .eventId(eventId)
                .message(message)
                .nabatGroupId(nabatGroupId)
                .notifiedByUserId(notifiedById)
                .build();
    }

    @Override
    public NotificationLogRequestDTO notificationLogRequestDTOBuilder(String jsonResponse){
        return NotificationLogRequestDTO.builder()
                .jsonResponse(jsonResponse)
                .build();
    }
}
