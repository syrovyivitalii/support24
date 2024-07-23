package lv.dsns.support24.user.repository;

import lv.dsns.support24.user.repository.entity.SystemUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface SystemUsersRepository extends JpaRepository <SystemUsers, UUID>, JpaSpecificationExecutor<SystemUsers> {

    Optional<SystemUsers> findByEmail(String email);

    @Query("select u.id from SystemUsers u where u.email = ?1")
    Optional<UUID> findIdByEmail(String email);

    boolean existsByEmail(String email);
}
