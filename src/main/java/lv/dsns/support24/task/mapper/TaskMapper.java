package lv.dsns.support24.task.mapper;

import lv.dsns.support24.task.controller.dto.request.TaskRequestDTO;
import lv.dsns.support24.task.controller.dto.response.TaskResponseDTO;
import lv.dsns.support24.task.repository.entity.Tasks;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    Tasks mapToEntity(TaskRequestDTO taskRequestDTO);

    TaskResponseDTO mapToDTO (Tasks tasks);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void patchMerge(TaskRequestDTO tasksDTO, @MappingTarget Tasks tasks);
}
