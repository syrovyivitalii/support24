package lv.dsns.support24.device.controller;

import lv.dsns.support24.common.dto.response.PageResponse;
import lv.dsns.support24.device.controller.dto.request.DeviceRequestDTO;
import lv.dsns.support24.device.controller.dto.request.DeviceWriteOffRequestDTO;
import lv.dsns.support24.device.controller.dto.response.DeviceResponseDTO;
import lv.dsns.support24.device.service.DeviceService;
import lv.dsns.support24.device.service.filter.DeviceFilter;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1")
public class DeviceController {
    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping("/private/devices/pageable")
    public ResponseEntity<PageResponse<DeviceResponseDTO>> getAllDevices (@ParameterObject DeviceFilter deviceFilter, @ParameterObject Pageable pageable){
       PageResponse<DeviceResponseDTO> responseDTOS = deviceService.findAllDevices(deviceFilter,pageable);
        return ResponseEntity.ok(responseDTOS);
    }

    @PostMapping("/private/devices")
    public ResponseEntity<DeviceResponseDTO> save (@RequestBody DeviceRequestDTO requestDTO){
        var savedDevice = deviceService.save(requestDTO);
        return ResponseEntity.ok(savedDevice);
    }
    @PatchMapping("/private/devices/write-off/{id}")
    public ResponseEntity<DeviceResponseDTO> writeOffDevice (@PathVariable UUID id, @RequestBody DeviceWriteOffRequestDTO requestDTO){
        var writtenOffDevice = deviceService.writeOffDevice(id, requestDTO);
        return ResponseEntity.ok(writtenOffDevice);
    }
    @PatchMapping("/private/devices/{id}")
    public ResponseEntity<DeviceResponseDTO> patchDevice (@PathVariable UUID id, @RequestBody DeviceRequestDTO requestDTO){
        var patchedDevice = deviceService.patchDevice(id, requestDTO);
        return ResponseEntity.ok(patchedDevice);
    }
}
