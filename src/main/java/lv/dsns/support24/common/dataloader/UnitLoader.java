package lv.dsns.support24.common.dataloader;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.dsns.support24.unit.controller.dto.request.UnitRequestDTO;
import lv.dsns.support24.unit.service.UnitService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class UnitLoader implements Consumer<List<Map<String, Object>>> {
    private final UnitService unitService;

    @Override
    public void accept(List<Map<String, Object>> maps) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        maps.stream().filter(x -> x.containsKey("units"))
                .forEach(x ->
                        ((List<HashMap>) x.get("units")).forEach(y -> {
                            UnitRequestDTO unitRequestDTO = mapper.convertValue(y, UnitRequestDTO.class);
                            if (!unitService.existUnitByUnitName(unitRequestDTO.getUnitName())) {
                                unitService.save(unitRequestDTO);
                            }
                        })
                );
    }
}
