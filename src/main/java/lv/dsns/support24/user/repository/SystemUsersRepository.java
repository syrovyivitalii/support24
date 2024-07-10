package lv.dsns.support24.user.repository;

import lv.dsns.support24.task.repository.entity.Tasks;
import lv.dsns.support24.user.repository.entity.SystemUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface SystemUsersRepository extends JpaRepository <SystemUsers, Long>, JpaSpecificationExecutor<SystemUsers> {

    Optional<SystemUsers> findByEmail(String email);

    boolean existsByEmail(String email);
}
