package lv.dsns.support24.unit.repository;

import lv.dsns.support24.unit.repository.entity.Unit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UnitRepository extends JpaRepository<Unit,UUID>, JpaSpecificationExecutor<Unit> {
    boolean existsUnitsByUnitName(String unitName);

    @Query(value = """
    WITH RECURSIVE units_hierarchy AS (
        SELECT id, unit_name, unit_type, created_date, updated_date, parent_unit_id, location, street, status
        FROM support24.tbl_units
        WHERE id = :unitId AND status = 'ACTIVE'
        UNION ALL
        SELECT child.id, child.unit_name, child.unit_type, child.created_date, child.updated_date, child.parent_unit_id, child.location, child.street,child.status
        FROM support24.tbl_units child
        INNER JOIN units_hierarchy parent ON child.parent_unit_id = parent.id
    )
    SELECT id, unit_name, unit_type, created_date, updated_date, parent_unit_id, location, street, status
    FROM units_hierarchy
    ORDER BY unit_type;
    """, nativeQuery = true)
    List<Unit> findHierarchyByUnitId(UUID unitId);
}
