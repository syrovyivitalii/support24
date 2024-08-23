package lv.dsns.support24.task.repository;

import lv.dsns.support24.task.repository.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
    import java.util.UUID;

    public interface TaskRepository extends JpaRepository<Task,UUID>, JpaSpecificationExecutor<Task> {
    Optional<Task> findById (UUID id);

    @Query("SELECT t FROM Task t WHERE t.status = 'INPROGRESS' OR t.status = 'UNCOMPLETED'")
    List<Task> findNotCompletedTasks();

    @Query("SELECT t FROM Task t WHERE t.parentId = ?1")
    List<Task> findAllSubtasks(UUID parentId);

    Page<Task> findTasksByParentIdIsNull(Specification<Task> spec, Pageable pageable);

}
