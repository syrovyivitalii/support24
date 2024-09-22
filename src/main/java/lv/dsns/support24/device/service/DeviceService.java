package lv.dsns.support24.device.service;

import lv.dsns.support24.device.controller.dto.request.DeviceRequestDTO;
import lv.dsns.support24.device.controller.dto.request.DeviceWriteOffRequestDTO;
import lv.dsns.support24.device.controller.dto.response.DeviceResponseDTO;
import lv.dsns.support24.device.service.filter.DeviceFilter;

import java.util.List;
import java.util.UUID;

public interface DeviceService {
    List<DeviceResponseDTO> findAllDevices(DeviceFilter deviceFilter);
    DeviceResponseDTO save (DeviceRequestDTO requestDTO);
    DeviceResponseDTO writeOffDevice(UUID id, DeviceWriteOffRequestDTO requestDTO);
}
