package lv.dsns.support24.device.service;

import lv.dsns.support24.common.dto.response.PageResponse;
import lv.dsns.support24.device.controller.dto.request.DeviceRequestDTO;
import lv.dsns.support24.device.controller.dto.request.DeviceWriteOffRequestDTO;
import lv.dsns.support24.device.controller.dto.response.DeviceResponseDTO;
import lv.dsns.support24.device.service.filter.DeviceFilter;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface DeviceService {

    PageResponse<DeviceResponseDTO> findAllDevices(DeviceFilter deviceFilter, Pageable pageable);

    DeviceResponseDTO save (DeviceRequestDTO requestDTO);

    DeviceResponseDTO writeOffDevice(UUID id, DeviceWriteOffRequestDTO requestDTO);

    DeviceResponseDTO patchDevice(UUID id, DeviceRequestDTO requestDTO);
}
