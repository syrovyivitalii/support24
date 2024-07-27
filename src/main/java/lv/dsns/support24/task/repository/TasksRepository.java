package lv.dsns.support24.task.repository;

import lv.dsns.support24.task.repository.entity.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
    import java.util.UUID;

    public interface TasksRepository extends JpaRepository<Tasks,UUID>, JpaSpecificationExecutor<Tasks> {
    Optional<Tasks> findById (UUID id);
    @Query("SELECT t FROM Tasks t WHERE t.status = 'INPROGRESS' OR t.status = 'UNCOMPLETED'")
    List<Tasks> findNotCompletedTasks();
}
