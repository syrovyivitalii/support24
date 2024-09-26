package lv.dsns.support24.device.mapper;

import lv.dsns.support24.device.controller.dto.enums.DeviceStatus;
import lv.dsns.support24.device.controller.dto.request.DeviceRequestDTO;
import lv.dsns.support24.device.controller.dto.request.DeviceWriteOffRequestDTO;
import lv.dsns.support24.device.controller.dto.response.DeviceResponseDTO;
import lv.dsns.support24.device.repository.entity.Device;

import org.mapstruct.*;

@Mapper(componentModel = "spring", imports = {DeviceStatus.class})
public interface DeviceMapper {
    Device mapToEntity(DeviceRequestDTO deviceRequestDTO);

    DeviceResponseDTO mapToDTO (Device device);
    @Mapping(target = "inventoryNumber", expression = "java(null)")
    @Mapping(target = "deviceStatus", expression = "java(DeviceStatus.WRITTENOFF)")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchMergeWriteOff(DeviceWriteOffRequestDTO deviceRequestDTO, @MappingTarget Device device);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchMerge(DeviceRequestDTO requestDTO, @MappingTarget Device device);

}
