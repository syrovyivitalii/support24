package lv.dsns.support24.task.repository;

import lv.dsns.support24.task.repository.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
    import java.util.UUID;

    public interface TaskRepository extends JpaRepository<Task,UUID>, JpaSpecificationExecutor<Task> {
    Optional<Task> findById (UUID id);

    @Query("SELECT t FROM Task t WHERE t.parentId = ?1")
    List<Task> findAllSubtasks(UUID parentId);

    @Modifying
    @Query("UPDATE Task t set t.status = 'EXPIRED' where t.dueDate < ?1 AND t.status != 'COMPLETED'")
    void updateStatus(LocalDateTime currentDate);

}
