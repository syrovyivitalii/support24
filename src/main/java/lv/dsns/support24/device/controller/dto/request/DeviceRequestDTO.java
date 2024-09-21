package lv.dsns.support24.device.controller.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lv.dsns.support24.device.controller.dto.enums.DeviceType;


import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceRequestDTO {
    private String deviceName;
    private DeviceType deviceType;
    private String inventoryNumber;
    private String decreeNumber;
    private String macAddress;
    private String ipAddress;
    private Integer productionYear;
    private String note;
    private UUID unitId;
}
