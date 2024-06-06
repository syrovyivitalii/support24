package lv.dsns.support24.common.util.filter;

import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class SearchFilter {

    protected String search;

    protected Set<UUID> ids;

    public String getSearch() {
        return Optional.ofNullable(search)
                .filter(StringUtils::isNotBlank)
                .orElse("");
    }

}
