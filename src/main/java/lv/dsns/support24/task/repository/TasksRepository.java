package lv.dsns.support24.task.repository;

import lv.dsns.support24.task.repository.entity.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TasksRepository extends JpaRepository<Tasks,Long> {
}
