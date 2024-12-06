package lv.dsns.support24.common.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import lv.dsns.support24.common.entity.BaseEntity;
import lv.dsns.support24.device.controller.dto.enums.DeviceStatus;
import lv.dsns.support24.device.controller.dto.enums.DeviceType;
import lv.dsns.support24.task.controller.dto.enums.Priority;
import lv.dsns.support24.task.controller.dto.enums.Status;
import lv.dsns.support24.task.controller.dto.enums.Type;
import lv.dsns.support24.unit.controller.dto.enums.UnitStatus;
import lv.dsns.support24.unit.controller.dto.enums.UnitType;
import lv.dsns.support24.user.controller.dto.enums.Role;
import lv.dsns.support24.user.controller.dto.enums.Shift;
import lv.dsns.support24.user.controller.dto.enums.UserStatus;
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

    public static Specification<? extends BaseEntity> searchLikeStringWithJoin(String joinField, String targetField, String value) {
        return (root, query, criteriaBuilder) -> {
            if (value == null || value.isEmpty()) {
                return null;
            }
            Join<Object, Object> join = root.join(joinField, JoinType.LEFT);
            return criteriaBuilder.like(criteriaBuilder.lower(join.get(targetField)), "%" + value.toLowerCase() + "%");
        };
    }

    public static Specification<? extends BaseEntity> searchLikeStringWithTwoJoins(
            String firstJoinField, String secondJoinField, String targetField, String value) {
        return (root, query, criteriaBuilder) -> {
            if (value == null || value.isEmpty()) {
                return null;
            }
            Join<Object, Object> firstJoin = root.join(firstJoinField, JoinType.LEFT);
            Join<Object, Object> secondJoin = firstJoin.join(secondJoinField, JoinType.LEFT);

            return criteriaBuilder.like(criteriaBuilder.lower(secondJoin.get(targetField)), "%" + value.toLowerCase() + "%");
        };
    }


    public static Specification<? extends BaseEntity> searchFieldInCollectionOfIds(String field, Set<UUID> set) {
        return CollectionUtils.isNotEmpty(set) ?
                (r, rq, cb) -> r.get(field).in(set) :
                null;
    }

    public static Specification<? extends BaseEntity> searchFieldInCollectionOfIntegerIds(String field, Set<Integer> set) {
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
    public static Specification<? extends BaseEntity> searchByDateRange(String field, LocalDate startDate, LocalDate endDate) {
        return (root, query, builder) -> {
            if (startDate == null && endDate == null) {
                return builder.conjunction();
            }
            Path<LocalDate> datePath = root.get(field);
            if (startDate != null && endDate != null) {
                return builder.between(datePath, startDate, endDate);
            } else if (startDate != null) {
                return builder.greaterThanOrEqualTo(datePath, startDate);
            } else {
                return builder.lessThanOrEqualTo(datePath, endDate);
            }
        };
    }
    public static Specification<? extends BaseEntity> searchByDeviceProductionYearRange(String field, Integer startYear, Integer endYear) {
        return (root, query, builder) -> {
            if (startYear == null && endYear == null) {
                return builder.conjunction();
            }
            Path<Integer> yearPath = root.get(field);
            if (startYear != null && endYear != null) {
                return builder.between(yearPath, startYear, endYear);
            } else if (startYear != null) {
                return builder.greaterThanOrEqualTo(yearPath, startYear);
            } else {
                return builder.lessThanOrEqualTo(yearPath, endYear);
            }
        };
    }

    public static <T> Specification<? extends BaseEntity> searchOnField(String fieldName, Set<T> values) {
        return CollectionUtils.isNotEmpty(values) ?
                (root, query, builder) -> root.get(fieldName).in(values) : null;
    }
}
