package lv.dsns.support24.task.mapper;

import lv.dsns.support24.task.controller.dto.request.PatchByUserTaskRequestDTO;
import lv.dsns.support24.task.controller.dto.request.TaskRequestDTO;
import lv.dsns.support24.task.controller.dto.response.TaskResponseDTO;
import lv.dsns.support24.task.repository.entity.Task;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    Task mapToEntity(TaskRequestDTO taskRequestDTO);

    TaskResponseDTO mapToDTO (Task task);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchMerge(TaskRequestDTO tasksDTO, @MappingTarget Task task);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchMergeByUser(PatchByUserTaskRequestDTO patchByUserTaskRequestDTO, @MappingTarget Task task);

}
