package lv.dsns.support24.device.controller;

import lv.dsns.support24.device.controller.dto.request.DeviceRequestDTO;
import lv.dsns.support24.device.controller.dto.response.DeviceResponseDTO;
import lv.dsns.support24.device.service.DeviceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class DeviceController {
    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping("/private/devices")
    public ResponseEntity<List<DeviceResponseDTO>> getAllDevices (){
        var allDevices = deviceService.findAllDevices();
        return ResponseEntity.ok(allDevices);
    }

    @PostMapping("/private/devices")
    public ResponseEntity<DeviceResponseDTO> save (@RequestBody DeviceRequestDTO requestDTO){
        var savedDevice = deviceService.save(requestDTO);
        return ResponseEntity.ok(savedDevice);
    }
}
