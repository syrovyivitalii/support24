package lv.dsns.support24.device.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lv.dsns.support24.device.controller.dto.enums.DeviceStatus;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceWriteOffRequestDTO {

    private String decreeNumber;
    private String inventoryNumber;
    private DeviceStatus deviceStatus;
}
