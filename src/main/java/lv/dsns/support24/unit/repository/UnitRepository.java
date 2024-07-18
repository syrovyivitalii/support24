package lv.dsns.support24.unit.repository;

import lv.dsns.support24.unit.repository.entity.Units;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UnitRepository extends JpaRepository<Units,UUID> {
    boolean existsUnitsByUnitName(String unitName);
}
