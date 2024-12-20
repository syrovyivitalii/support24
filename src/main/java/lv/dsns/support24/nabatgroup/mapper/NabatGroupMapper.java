package lv.dsns.support24.nabatgroup.mapper;

import lv.dsns.support24.nabatgroup.controller.dto.request.NabatGroupRequestDTO;
import lv.dsns.support24.nabatgroup.controller.dto.response.NabatGroupResponseDTO;
import lv.dsns.support24.nabatgroup.repository.entity.NabatGroup;
import lv.dsns.support24.task.controller.dto.request.TaskRequestDTO;
import lv.dsns.support24.task.repository.entity.Task;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface NabatGroupMapper {

    NabatGroup mapToEntity(NabatGroupRequestDTO nabatGroupRequestDTO);

    NabatGroupResponseDTO mapToDTO(NabatGroup nabatGroup);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchMerge(NabatGroupRequestDTO nabatGroupRequestDTO, @MappingTarget NabatGroup nabatGroup);
}
