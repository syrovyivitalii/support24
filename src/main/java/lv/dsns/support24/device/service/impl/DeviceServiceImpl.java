package lv.dsns.support24.device.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.dsns.support24.device.controller.dto.response.DeviceResponseDTO;
import lv.dsns.support24.device.mapper.DeviceMapper;
import lv.dsns.support24.device.repository.DeviceRepository;
import lv.dsns.support24.device.service.DeviceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {
    private final DeviceRepository deviceRepository;
    private final DeviceMapper deviceMapper;

    @Override
    public List<DeviceResponseDTO> findAllDevices(){
        var allDevices = deviceRepository.findAll();
        return allDevices.stream().map(deviceMapper::mapToDTO).collect(Collectors.toList());
    }
}
