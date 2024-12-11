package lv.dsns.support24.user.repository;

import lv.dsns.support24.user.controller.dto.enums.Role;
import lv.dsns.support24.user.controller.dto.enums.UserStatus;
import lv.dsns.support24.user.repository.entity.SystemUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SystemUsersRepository extends JpaRepository <SystemUsers, UUID>, JpaSpecificationExecutor<SystemUsers> {

    Optional<SystemUsers> findByEmail(String email);

    @Query("select u.email from SystemUsers u where u.id = ?1")
    Optional<String> findEmailById(UUID id);

    @Query("select u.email from SystemUsers u where u.role = ?1")
    List<String> findEmailsByRole(Role role);

    Optional<SystemUsers> findByEmailAndStatus(String email, UserStatus status);

    boolean existsByEmail(String email);

}
