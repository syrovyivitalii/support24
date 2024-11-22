package lv.dsns.support24.phone.service.filter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import lv.dsns.support24.common.util.filter.SearchFilter;
import lv.dsns.support24.phone.controller.dto.enums.PhoneType;

import java.util.Set;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@FieldDefaults(level = PRIVATE)
public class PhoneFilter extends SearchFilter {

    private Set<UUID> userIds;
    private String phone;
    private Set<PhoneType> phoneType;

}
