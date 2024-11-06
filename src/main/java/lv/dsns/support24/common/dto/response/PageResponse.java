package lv.dsns.support24.common.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class PageResponse <T>{

    private Long totalPages;
    private Long pageSize;
    private Long totalElements;
    private List<T> content = new ArrayList<>();
}
