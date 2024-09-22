package lv.dsns.support24.device.repository;

import lv.dsns.support24.device.repository.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device,UUID>, JpaSpecificationExecutor<Device> {
    boolean existsDeviceByInventoryNumber (String inventoryNumber);
}
