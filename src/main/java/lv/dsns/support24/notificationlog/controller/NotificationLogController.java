package lv.dsns.support24.notificationlog.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lv.dsns.support24.common.dto.response.PageResponse;
import lv.dsns.support24.notificationlog.controller.dto.request.NotificationLogRequestDTO;
import lv.dsns.support24.notificationlog.controller.dto.response.NotificationLogResponseDTO;
import lv.dsns.support24.notificationlog.service.NotificationLogService;
import lv.dsns.support24.notificationlog.controller.dto.response.NotifiedUsersResponseDTO;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/private/notification-log")
@RequiredArgsConstructor
public class NotificationLogController {

    private final NotificationLogService notificationLogService;

    @PostMapping
    public ResponseEntity<NotificationLogResponseDTO> save (NotificationLogRequestDTO notificationLogRequestDTO) {
        var savedNotificationLog = notificationLogService.save(notificationLogRequestDTO);

        return ResponseEntity.ok(savedNotificationLog);
    }

    @GetMapping("/by-nabat-group/{nabatGroupId}/pageable")
    public ResponseEntity<PageResponse<NotificationLogResponseDTO>> findByNabatGroup (@PathVariable("nabatGroupId") UUID nabatGroupId,
                                                                                      @SortDefault(sort = "createdDate", direction = Sort.Direction.DESC)
                                                                                      @ParameterObject Pageable pageable) {
        PageResponse<NotificationLogResponseDTO> responseDTOs = notificationLogService.findByNabatGroup(nabatGroupId, pageable);

        return ResponseEntity.ok(responseDTOs);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<NotificationLogResponseDTO> patch (@PathVariable UUID id, @RequestBody NotificationLogRequestDTO notificationLogRequestDTO) {
        var patched = notificationLogService.patch(id, notificationLogRequestDTO);

        return ResponseEntity.ok(patched);
    }

    @PostMapping("/notified-users/by-event-id/{eventId}")
    public ResponseEntity<NotifiedUsersResponseDTO> getNotificationInfo (@PathVariable UUID eventId) {
        var notificationInfo = notificationLogService.getNotificationInfo(eventId);

        return ResponseEntity.ok(notificationInfo);
    }

    @GetMapping("/notified-users/by-event-id/to-csv/{eventId}")
    public ResponseEntity<Void> exportNotifyInfoToCSV(@PathVariable UUID eventId, HttpServletResponse response) throws IOException{

        notificationLogService.getNotifyInfoToCsv(response, eventId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
