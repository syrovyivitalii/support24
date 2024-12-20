package lv.dsns.support24.phone.repository;

import lv.dsns.support24.phone.repository.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface PhoneRepository extends JpaRepository<Phone, UUID> , JpaSpecificationExecutor<Phone> {
}
