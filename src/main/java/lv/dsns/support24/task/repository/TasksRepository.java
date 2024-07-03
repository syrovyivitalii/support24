package lv.dsns.support24.task.repository;

import lv.dsns.support24.task.repository.entity.Tasks;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
    import java.util.UUID;

    public interface TasksRepository extends JpaRepository<Tasks,Long>, JpaSpecificationExecutor<Tasks> {
    Optional<Tasks> findById (UUID id);
}
