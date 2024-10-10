package lv.dsns.support24.position.repository;

import lv.dsns.support24.position.repository.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PositionRepository extends JpaRepository<Position, UUID> {
    boolean existsByPositionName(String positionName);
}


