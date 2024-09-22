package lv.dsns.support24.device.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.dsns.support24.common.exception.ClientBackendException;
import lv.dsns.support24.common.exception.ErrorCode;
import lv.dsns.support24.device.controller.dto.enums.DeviceType;
import lv.dsns.support24.device.controller.dto.request.DeviceRequestDTO;
import lv.dsns.support24.device.controller.dto.request.DeviceWriteOffRequestDTO;
import lv.dsns.support24.device.controller.dto.response.DeviceResponseDTO;
import lv.dsns.support24.device.mapper.DeviceMapper;
import lv.dsns.support24.device.repository.DeviceRepository;
import lv.dsns.support24.device.repository.entity.Device;
import lv.dsns.support24.device.service.DeviceService;
import lv.dsns.support24.device.service.filter.DeviceFilter;
import lv.dsns.support24.task.repository.entity.Task;
import lv.dsns.support24.task.service.filter.TaskFilter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static lv.dsns.support24.common.specification.SpecificationCustom.*;
import static lv.dsns.support24.common.specification.SpecificationCustom.searchByDateRange;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {
    private final DeviceRepository deviceRepository;
    private final DeviceMapper deviceMapper;

    @Override
    public List<DeviceResponseDTO> findAllDevices(DeviceFilter deviceFilter){
        var allDevices = deviceRepository.findAll(getSearchSpecification(deviceFilter));
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

    private Specification<Device> getSearchSpecification(DeviceFilter deviceFilter) {
        return Specification.where((Specification<Device>) searchLikeString("deviceName", deviceFilter.getDeviceName()))
                .and((Specification<Device>) searchOnDeviceType(deviceFilter.getDeviceTypes()))
                .and((Specification<Device>) searchLikeString("inventoryNumber", deviceFilter.getInventoryNumber()))
                .and((Specification<Device>) searchLikeString("decreeNumber", deviceFilter.getDecreeNumber()))
                .and((Specification<Device>) searchByDeviceProductionYearRange("productionYear", deviceFilter.getStartYear(), deviceFilter.getEndYear()))
                .and((Specification<Device>) searchFieldInCollectionOfIds("unitId", deviceFilter.getUnitIds()))
                .and((Specification<Device>) searchOnDeviceStatus(deviceFilter.getDeviceStatuses()));
    }
}
