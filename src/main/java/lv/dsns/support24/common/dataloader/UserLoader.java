package lv.dsns.support24.common.dataloader;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.dsns.support24.user.controller.dto.request.UserRequestDTO;
import lv.dsns.support24.user.service.UserService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserLoader implements Consumer<List<Map<String, Object>>> {

    private final UserService userService;

    @Override
    public void accept(List<Map<String, Object>> maps) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        maps.stream().filter(x -> x.containsKey("users"))
                .forEach(x ->
                        ((List<HashMap>) x.get("users")).forEach(y -> {
                            UserRequestDTO userRequestDto = mapper.convertValue(y, UserRequestDTO.class);
                            if (!userService.existUserByEmail(userRequestDto.getEmail())) {
                                userService.save(userRequestDto);
                            }
                        })
                );

    }
}
