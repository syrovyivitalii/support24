package lv.dsns.support24.user.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import lv.dsns.support24.common.entity.BaseEntity;
import lv.dsns.support24.device.repository.entity.Device;
import lv.dsns.support24.nabat.repository.entity.Nabat;
import lv.dsns.support24.notificationlog.repository.entity.NotificationLog;
import lv.dsns.support24.phone.repository.entity.Phone;
import lv.dsns.support24.position.repository.entity.Position;
import lv.dsns.support24.rank.repository.entity.Rank;
import lv.dsns.support24.task.repository.entity.Task;
import lv.dsns.support24.unit.repository.entity.Unit;
import lv.dsns.support24.user.controller.dto.enums.Role;
import lv.dsns.support24.user.controller.dto.enums.Shift;
import lv.dsns.support24.user.controller.dto.enums.UserStatus;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "tbl_system_users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class SystemUsers extends BaseEntity implements UserDetails {

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private Shift shift;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private Role role;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private UserStatus status;

    private boolean verify;

    private String name;

    @Column(name = "position_id")
    private UUID positionId;

    @Column(name = "user_unit_id")
    private UUID unitId;

    @Column(name = "permission_unit_id")
    private UUID permissionUnitId;

    @Column(name = "rank_id")
    private UUID rankId;

    @Column(name = "sodu_id")
    private Integer soduId;

    @OneToMany(mappedBy = "assignedFor", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<Task> assignedForTasks;

    @OneToMany(mappedBy = "assignedBy", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<Task> assignedByTasks;

    @OneToMany(mappedBy = "createdBy", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<Task> createdByTasks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_unit_id", referencedColumnName = "id",nullable = false, insertable = false, updatable = false)
    private Unit userUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_unit_id", referencedColumnName = "id",nullable = false, insertable = false, updatable = false)
    private Unit userPermittedUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rank_id", referencedColumnName = "id",nullable = false, insertable = false, updatable = false)
    private Rank userRank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id", referencedColumnName = "id",nullable = false, insertable = false, updatable = false)
    private Position userPosition;

    @OneToMany(mappedBy = "nabatUsers", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<Nabat> userNabat;

    @OneToMany(mappedBy = "phoneUser", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<Phone> userPhones;

    @OneToMany(mappedBy = "deviceUser", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<Device> userDevices;

    @OneToMany(mappedBy = "notificationLogUser", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<NotificationLog> userNotificationLogs;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    @Override
    public String getPassword(){
        return password;
    }
    @PrePersist
    protected void onCreate() {
        if (role == null) {
            role = Role.ROLE_USER;
        }
        if (status == null){
            status = UserStatus.ACTIVE;
        }
        if (shift == null){
            shift = Shift.Відсутня;
        }
    }
}
