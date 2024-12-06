package lv.dsns.support24.task.service.filter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import lv.dsns.support24.common.util.filter.SearchFilter;
import lv.dsns.support24.task.controller.dto.enums.Priority;
import lv.dsns.support24.task.controller.dto.enums.Status;
import lv.dsns.support24.task.controller.dto.enums.Type;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@FieldDefaults(level = PRIVATE)
public class TaskFilter extends SearchFilter {

    Set<UUID> assignedForIds;
    Set<UUID> assignedByIds;
    Set<UUID> createdByIds;
    Set<UUID> problemTypeIds;
    Set<Status> statuses;
    Set<Priority> priorities;
    Set<Type> taskTypes;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate endDate;

}
