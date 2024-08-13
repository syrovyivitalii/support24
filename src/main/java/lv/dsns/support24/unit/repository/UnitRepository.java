package lv.dsns.support24.unit.repository;

import lv.dsns.support24.unit.repository.entity.Units;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface UnitRepository extends JpaRepository<Units,UUID>, JpaSpecificationExecutor<Units> {
    boolean existsUnitsByUnitName(String unitName);
}
