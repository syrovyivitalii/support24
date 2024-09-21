package lv.dsns.support24.device.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.dsns.support24.common.exception.ClientBackendException;
import lv.dsns.support24.common.exception.ErrorCode;
import lv.dsns.support24.device.controller.dto.request.DeviceRequestDTO;
import lv.dsns.support24.device.controller.dto.request.DeviceWriteOffRequestDTO;
import lv.dsns.support24.device.controller.dto.response.DeviceResponseDTO;
import lv.dsns.support24.device.mapper.DeviceMapper;
import lv.dsns.support24.device.repository.DeviceRepository;
import lv.dsns.support24.device.service.DeviceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
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
    @Override
    @Transactional
    public DeviceResponseDTO save (DeviceRequestDTO requestDTO){
        var device = deviceMapper.mapToEntity(requestDTO);
        if (deviceRepository.existsDeviceByInventoryNumber(requestDTO.getInventoryNumber())){
            throw new ClientBackendException(ErrorCode.INVENTORY_NUMBER_ALREADY_EXISTS);
        }
        var savedDevice = deviceRepository.save(device);
        return deviceMapper.mapToDTO(savedDevice);
    }
    @Override
    @Transactional
    public DeviceResponseDTO writeOffDevice(UUID id, DeviceWriteOffRequestDTO requestDTO){
        var deviceById = deviceRepository.findById(id)
                .orElseThrow(() -> new ClientBackendException(ErrorCode.DEVICE_NOT_FOUND));

        deviceMapper.patchMerge(requestDTO, deviceById);

        return deviceMapper.mapToDTO(deviceById);
    }
}
