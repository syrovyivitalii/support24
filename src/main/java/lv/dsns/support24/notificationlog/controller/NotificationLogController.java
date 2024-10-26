package lv.dsns.support24.notificationlog.controller;

import lombok.RequiredArgsConstructor;
import lv.dsns.support24.common.dto.response.PageResponse;
import lv.dsns.support24.notificationlog.controller.dto.request.NotificationLogRequestDTO;
import lv.dsns.support24.notificationlog.controller.dto.response.NotificationLogResponseDTO;
import lv.dsns.support24.notificationlog.service.NotificationLogService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/by-nagat-group/{nabatGroupId}")
    public ResponseEntity<PageResponse<NotificationLogResponseDTO>> findByNabatGroup (@PathVariable("nabatGroupId") UUID nabatGroupId,
                                                                                      @SortDefault(sort = "createdDate", direction = Sort.Direction.ASC)
                                                                                      @ParameterObject Pageable pageable) {
        PageResponse<NotificationLogResponseDTO> responseDTOs = notificationLogService.findByNabatGroup(nabatGroupId, pageable);

        return ResponseEntity.ok(responseDTOs);
    }
}
