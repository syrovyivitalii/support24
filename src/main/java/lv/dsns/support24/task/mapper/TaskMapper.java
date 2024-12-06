package lv.dsns.support24.task.mapper;

import lv.dsns.support24.task.controller.dto.enums.Status;
import lv.dsns.support24.task.controller.dto.request.PatchByUserTaskRequestDTO;
import lv.dsns.support24.task.controller.dto.request.TaskRequestDTO;
import lv.dsns.support24.task.controller.dto.response.TaskResponseDTO;
import lv.dsns.support24.task.repository.entity.Task;
import org.mapstruct.*;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class, Status.class})
public interface TaskMapper {

    Task mapToEntity(TaskRequestDTO taskRequestDTO);

    TaskResponseDTO mapToDTO (Task task);

    @Mapping(target = "completedDate", expression = "java(tasksDTO.getStatus() == Status.COMPLETED ? LocalDateTime.now() : null)")
    @Mapping(target = "updatedDate", expression = "java(LocalDateTime.now())")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchMerge(TaskRequestDTO tasksDTO, @MappingTarget Task task);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchMergeByUser(PatchByUserTaskRequestDTO patchByUserTaskRequestDTO, @MappingTarget Task task);

}
