package lv.dsns.support24.device.repository;

import lv.dsns.support24.device.repository.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device,UUID> {
    boolean existsDeviceByInventoryNumber (String inventoryNumber);
}
