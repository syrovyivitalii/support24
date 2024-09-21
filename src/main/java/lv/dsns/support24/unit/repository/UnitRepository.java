package lv.dsns.support24.unit.repository;

import lv.dsns.support24.unit.repository.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface UnitRepository extends JpaRepository<Unit,UUID>, JpaSpecificationExecutor<Unit> {
    boolean existsUnitsByUnitName(String unitName);
    List<Unit> findByParentUnitIdNotNull();
    List<Unit> findByParentUnitIdAndGroupId(UUID parentId, int groupId);
}
