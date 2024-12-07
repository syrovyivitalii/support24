package lv.dsns.support24.common.dataloader.loader;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.dsns.support24.position.controller.dto.request.PositionRequestDTO;
import lv.dsns.support24.position.service.PositionService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class PositionLoader implements Consumer<List<Map<String, Object>>> {
    private final PositionService positionService;
    @Override
    public void accept(List<Map<String, Object>> maps) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        maps.stream().filter(x -> x.containsKey("positions"))
                .forEach(x ->
                        ((List<HashMap>) x.get("positions")).forEach(y -> {
                            PositionRequestDTO positionRequestDTO = mapper.convertValue(y, PositionRequestDTO.class);
                            if (!positionService.existsByPositionName(positionRequestDTO.getPositionName())) {
                                positionService.save(positionRequestDTO);
                            }
                        })
                );
    }
}
