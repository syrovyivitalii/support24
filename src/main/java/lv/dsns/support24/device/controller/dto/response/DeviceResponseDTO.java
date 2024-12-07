package lv.dsns.support24.device.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lv.dsns.support24.device.controller.dto.enums.DeviceStatus;
import lv.dsns.support24.device.controller.dto.enums.DeviceType;
import lv.dsns.support24.unit.controller.dto.response.UnitResponseDTO;
import lv.dsns.support24.user.controller.dto.response.UserResponseDTO;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceResponseDTO {

    private UUID id;
    private String deviceName;
    private DeviceType deviceType;
    private String inventoryNumber;
    private String decreeNumber;
    private String macAddress;
    private String ipAddress;
    private Integer productionYear;
    private UserResponseDTO deviceUser;
    private String specifications;
    private String note;
    private DeviceStatus deviceStatus;
    private UUID unitId;
    private UnitResponseDTO deviceUnit;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
