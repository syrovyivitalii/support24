package lv.dsns.support24.notificationlog.service.impl;

import lombok.RequiredArgsConstructor;
import lv.dsns.support24.common.dto.response.PageResponse;
import lv.dsns.support24.notificationlog.controller.dto.request.NotificationLogRequestDTO;
import lv.dsns.support24.notificationlog.controller.dto.response.NotificationLogResponseDTO;
import lv.dsns.support24.notificationlog.mapper.NotificationLogMapper;
import lv.dsns.support24.notificationlog.repository.NotificationLogRepository;
import lv.dsns.support24.notificationlog.repository.entity.NotificationLog;
import lv.dsns.support24.notificationlog.service.NotificationLogService;
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

    @Override
    public NotificationLogRequestDTO notificationLogRequestDTOBuilder(UUID notificationLogId,
                                                                      UUID nabatGroupId,
                                                                      UUID notifiedById,
                                                                      String message) {
        return NotificationLogRequestDTO.builder()
                .notificationId(notificationLogId)
                .message(message)
                .nabatGroupId(nabatGroupId)
                .notifiedByUserId(notifiedById)
                .build();
    }
}
