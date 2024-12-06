package lv.dsns.support24.notificationlog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
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
import lv.dsns.support24.notificationlog.controller.dto.response.NotifiedUsersResponseDTO;
import lv.dsns.support24.notifyresult.dto.NotifyResultResponseDTO;
import lv.dsns.support24.user.controller.dto.response.UserResponseDTO;
import lv.dsns.support24.user.service.UserService;
import lv.dsns.support24.user.service.filter.UserFilter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationLogServiceImpl implements NotificationLogService {

    private final NotificationLogMapper notificationLogMapper;
    private final NotificationLogRepository notificationLogRepository;
    private final UserService userService;


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
    public NotifiedUsersResponseDTO getNotificationInfo(UUID eventId) {
        NotificationLog notificationLog = notificationLogRepository.findByEventId(eventId).orElseThrow(() -> new ClientBackendException(ErrorCode.NOTIFICATION_LOG_NOT_FOUND));

        String jsonResponse = notificationLog.getJsonResponse();

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            NotifyResultResponseDTO notifyResult  = objectMapper.readValue(jsonResponse, NotifyResultResponseDTO.class);

            List<Integer> soduIds = notifyResult.getNotifiedUsers().stream()
                    .map(NotifyResultResponseDTO.NotifyResultInfo::getSoduId)
                    .toList();

            UserFilter userFilter = new UserFilter();
            userFilter.setSoduId(new HashSet<>(soduIds));
            List<UserResponseDTO> allUsers = userService.findAll(userFilter);

            Map<Integer, String> mapperSoduIdAndNames = allUsers.stream()
                    .collect(Collectors.toMap(UserResponseDTO::getSoduId, UserResponseDTO::getName));

            List<NotifiedUsersResponseDTO.NotifiedUserInfo> notifiedUsers = notifyResult.getNotifiedUsers().stream()
                    .map(userInfo -> new NotifiedUsersResponseDTO.NotifiedUserInfo(
                            userInfo.getSoduId(),
                            mapperSoduIdAndNames.get(userInfo.getSoduId()),
                            userInfo.getMobilePhone(),
                            userInfo.isNotifyStatus()
                    ))
                    .toList();

            return NotifiedUsersResponseDTO.builder()
                    .notifiedUsers(notifiedUsers)
                    .build();
        } catch (Exception e) {
            throw new ClientBackendException(ErrorCode.NOTIFICATION_INFO_NOT_FOUND);
        }
    }

    @Override
    public void getNotifyInfoToCsv(HttpServletResponse response, UUID eventId) throws IOException {

        NotificationLog notificationLog = notificationLogRepository.findByEventId(eventId)
                .orElseThrow(() -> new ClientBackendException(ErrorCode.NOTIFICATION_LOG_NOT_FOUND));

        var notificationLogResponseDTO = notificationLogMapper.mapToDTO(notificationLog);

        NotifiedUsersResponseDTO notificationInfo = getNotificationInfo(eventId);

        response.setContentType("text/csv; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"notify.csv\"");

        try (CSVPrinter csvPrinter = new CSVPrinter(response.getWriter(), CSVFormat.DEFAULT)) {
            csvPrinter.printRecord("Група оповіщення", notificationLogResponseDTO.getNotificationLogGroup().getGroupName());
            csvPrinter.printRecord("Повідомлення", notificationLogResponseDTO.getMessage());
            csvPrinter.printRecord("Дата", notificationLogResponseDTO.getCreatedDate());
            csvPrinter.printRecord("Оператор оповіщення", notificationLogResponseDTO.getNotificationLogUser().getName());
            csvPrinter.println();

            Map<Boolean, List<NotifiedUsersResponseDTO.NotifiedUserInfo>> groupedUsers =
                    notificationInfo.getNotifiedUsers()
                            .stream()
                            .collect(Collectors.partitioningBy(NotifiedUsersResponseDTO.NotifiedUserInfo::isNotifyStatus));

            printUserGroup(csvPrinter, groupedUsers.get(true), "Оповіщено");
            printUserGroup(csvPrinter, groupedUsers.get(false), "Не оповіщено");
        }
    }

    private void printUserGroup(CSVPrinter csvPrinter, List<NotifiedUsersResponseDTO.NotifiedUserInfo> users, String status) throws IOException {
        csvPrinter.printRecord("Абонент", "Номер телефону", "Статус: " + status);
        for (NotifiedUsersResponseDTO.NotifiedUserInfo user : users) {
            csvPrinter.printRecord(user.getName(), user.getMobilePhone(), status);
        }
        csvPrinter.println();
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

    @Override
    public boolean isLatestNotificationLog(UUID nabatGroupId, UUID eventId){
        Optional<NotificationLog> latestNotificationLog = notificationLogRepository.findTopByNabatGroupIdOrderByCreatedDateDesc(nabatGroupId);

        return latestNotificationLog.isPresent() && latestNotificationLog.get().getEventId().equals(eventId);

    }


}
