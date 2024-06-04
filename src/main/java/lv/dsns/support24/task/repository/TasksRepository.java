package lv.dsns.support24.task.repository;

import lv.dsns.support24.task.repository.entity.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TasksRepository extends JpaRepository<Tasks,Long> {
    Optional<Tasks> findById (UUID id);
}
