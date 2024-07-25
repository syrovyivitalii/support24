package lv.dsns.support24.common.specification;

import jakarta.persistence.criteria.Path;
import lv.dsns.support24.common.entity.BaseEntity;
import lv.dsns.support24.task.controller.dto.enums.Priority;
import lv.dsns.support24.task.controller.dto.enums.Status;
import lv.dsns.support24.task.repository.entity.Tasks;
import lv.dsns.support24.unit.controller.dto.enums.UnitType;
import lv.dsns.support24.user.controller.dto.enums.Role;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
public class SpecificationCustom {
    public static Specification<? extends BaseEntity> distinct() {
        return (root, query, cb) -> {
            query.distinct(true);
            return null;
        };
    }
    public static Specification<? extends BaseEntity> searchLikeString(String propertyName, String name) {
        return Optional.ofNullable(name)
                .filter(StringUtils::isNotBlank)
                .map(property -> (Specification<? extends BaseEntity>)
                        (r, rq, cb) -> cb.like(cb.lower(r.get(propertyName)), "%" + property.toLowerCase().trim() + "%"))
                    .orElse(null);
    }

    public static Specification<? extends BaseEntity> searchFieldInCollectionOfIds(String field, Set<UUID> set) {
        return CollectionUtils.isNotEmpty(set) ?
                (r, rq, cb) -> r.get(field).in(set) :
                null;
    }

    public static Specification<? extends BaseEntity> searchFieldInCollectionOfJoinedIds(String joinField1, String joinField2, String field, Set<UUID> set) {
        return CollectionUtils.isNotEmpty(set) ?
                (r, rq, cb) -> r.join(joinField1).join(joinField2).get(field).in(set) :
                null;
    }

    public static Specification<? extends BaseEntity> searchOnString(String field, String value) {
        return (root, query, builder) -> {
            if (value == null || value.isEmpty()) {
                return builder.conjunction();
            }
            return builder.equal(root.get(field), value);
        };
    }

    public static Specification<? extends BaseEntity> searchByDueDate(String field, LocalDate value) {
        return (root, query, builder) -> {
            if (value == null) {
                return builder.conjunction();
            }
            Path<LocalDate> datePath = root.get(field);
            return builder.equal(datePath, value);
        };
    }

    public static Specification<? extends BaseEntity> searchOnStatus(Set<Status> statuses) {
        return CollectionUtils.isNotEmpty(statuses)?
                (root, query, builder) -> root.get("status").in(statuses):null;
    }
    public static Specification<? extends BaseEntity> searchOnPriority(Set<Priority> priorities) {
        return CollectionUtils.isNotEmpty(priorities)?
                (root, query, builder) -> root.get("priority").in(priorities):null;
    }

    public static Specification<? extends BaseEntity> searchOnRole(Set<Role> roles) {
        return CollectionUtils.isNotEmpty(roles)?
                (root, query, builder) -> root.get("role").in(roles):null;
    }


    public static Specification<? extends BaseEntity> searchOnUnitType(Set<UnitType> types) {
        return CollectionUtils.isNotEmpty(types)?
                (root, query, builder) -> root.get("unitType").in(types):null;
    }

}
