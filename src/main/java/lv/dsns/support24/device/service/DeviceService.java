package lv.dsns.support24.device.service;

import lv.dsns.support24.device.controller.dto.response.DeviceResponseDTO;

import java.util.List;

public interface DeviceService {
    List<DeviceResponseDTO> findAllDevices();
}
