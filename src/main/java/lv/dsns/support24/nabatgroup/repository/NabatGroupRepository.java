package lv.dsns.support24.nabatgroup.repository;

import lv.dsns.support24.nabatgroup.repository.entity.NabatGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NabatGroupRepository extends JpaRepository<NabatGroup, UUID> {

    List<NabatGroup> findByUnitId(UUID unitId);

    boolean existsByGroupNameAndUnitId(String groupName, UUID unitId);

    boolean existsByNabatGroupId(UUID nabatGroupId);

}
