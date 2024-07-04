package lv.dsns.support24.user.repository;

import lv.dsns.support24.user.repository.entity.SystemUsers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SystemUsersRepository extends JpaRepository <SystemUsers, Long> {

    Optional<SystemUsers> findByEmail(String email);

    boolean existsByEmail(String email);
}
