package lv.dsns.support24.notificationlog.mapper;

import lv.dsns.support24.notificationlog.controller.dto.request.NotificationLogRequestDTO;
import lv.dsns.support24.notificationlog.controller.dto.response.NotificationLogResponseDTO;
import lv.dsns.support24.notificationlog.repository.entity.NotificationLog;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationLogMapper {

    NotificationLog mapToEntity(NotificationLogRequestDTO notificationLogRequestDTO);

    NotificationLogResponseDTO mapToDTO(NotificationLog notificationLog);
}
