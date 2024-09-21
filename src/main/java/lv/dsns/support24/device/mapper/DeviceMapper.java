package lv.dsns.support24.device.mapper;

import lv.dsns.support24.device.controller.dto.request.DeviceRequestDTO;
import lv.dsns.support24.device.controller.dto.response.DeviceResponseDTO;
import lv.dsns.support24.device.repository.entity.Device;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeviceMapper {
    Device mapToEntity(DeviceRequestDTO deviceRequestDTO);

    DeviceResponseDTO mapToDTO (Device device);
}
