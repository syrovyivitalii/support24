package lv.dsns.support24.common.specification;

import jakarta.persistence.criteria.Path;
import lv.dsns.support24.common.entity.BaseEntity;
import lv.dsns.support24.task.repository.entity.Tasks;
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

    public static Specification<Tasks> searchOnString(String field, String value) {
        return (root, query, builder) -> {
            if (value == null || value.isEmpty()) {
                return builder.conjunction();
            }
            return builder.equal(root.get(field), value);
        };
    }

    public static Specification<Tasks> searchByDueDate(String field, LocalDate value) {
        return (root, query, builder) -> {
            if (value == null) {
                return builder.conjunction();
            }
            Path<LocalDate> datePath = root.get(field);
            return builder.equal(datePath, value);
        };
    }

}
