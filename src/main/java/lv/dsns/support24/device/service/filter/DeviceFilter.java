package lv.dsns.support24.device.service.filter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import lv.dsns.support24.common.util.filter.SearchFilter;
import lv.dsns.support24.device.controller.dto.enums.DeviceStatus;
import lv.dsns.support24.device.controller.dto.enums.DeviceType;

import java.util.Set;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@FieldDefaults(level = PRIVATE)
public class DeviceFilter extends SearchFilter {

    String deviceName;
    Set<DeviceType> deviceTypes;
    String inventoryNumber;
    String decreeNumber;
    Set<DeviceStatus> deviceStatuses;
    Set<UUID> unitIds;
    Set<UUID> deviceIds;
    Set<UUID> userIds;
    Integer startYear;
    Integer endYear;
    String macAddress;
    String ipAddress;
}
