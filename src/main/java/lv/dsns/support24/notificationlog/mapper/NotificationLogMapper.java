package lv.dsns.support24.notificationlog.mapper;

import lv.dsns.support24.notificationlog.controller.dto.request.NotificationLogRequestDTO;
import lv.dsns.support24.notificationlog.controller.dto.response.NotificationLogResponseDTO;
import lv.dsns.support24.notificationlog.repository.entity.NotificationLog;
import lv.dsns.support24.task.controller.dto.request.TaskRequestDTO;
import lv.dsns.support24.task.repository.entity.Task;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface NotificationLogMapper {

    NotificationLog mapToEntity(NotificationLogRequestDTO notificationLogRequestDTO);

    NotificationLogResponseDTO mapToDTO(NotificationLog notificationLog);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchMerge(NotificationLogRequestDTO requestDTO, @MappingTarget NotificationLog notificationLog);
}
