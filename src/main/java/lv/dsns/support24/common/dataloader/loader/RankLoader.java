package lv.dsns.support24.common.dataloader.loader;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.dsns.support24.rank.controller.dto.request.RankRequestDTO;
import lv.dsns.support24.rank.service.RankService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class RankLoader implements Consumer<List<Map<String, Object>>> {
    private final RankService rankService;

    @Override
    public void accept(List<Map<String, Object>> maps) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        maps.stream().filter(x -> x.containsKey("ranks"))
                .forEach(x ->
                        ((List<HashMap>) x.get("ranks")).forEach(y -> {
                            RankRequestDTO rankRequestDTO = mapper.convertValue(y, RankRequestDTO.class);
                            if (!rankService.existsByRankName(rankRequestDTO.getRankName())) {
                                rankService.save(rankRequestDTO);
                            }
                        })
                );
    }
}
