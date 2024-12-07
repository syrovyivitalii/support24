package lv.dsns.support24.common.dataloader.loader;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.dsns.support24.problem.controller.dto.request.ProblemRequestDTO;
import lv.dsns.support24.problem.service.ProblemService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProblemLoader implements Consumer<List<Map<String, Object>>> {
    private final ProblemService problemService;
    @Override
    public void accept(List<Map<String, Object>> maps) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        maps.stream().filter(x -> x.containsKey("problems"))
                .forEach(x ->
                        ((List<HashMap>) x.get("problems")).forEach(y -> {
                            ProblemRequestDTO problemRequestDTO = mapper.convertValue(y, ProblemRequestDTO.class);
                            if (!problemService.existByProblem(problemRequestDTO.getProblem())) {
                                problemService.save(problemRequestDTO);
                            }
                        })
                );
    }
}
