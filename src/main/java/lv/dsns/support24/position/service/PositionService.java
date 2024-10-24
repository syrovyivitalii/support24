package lv.dsns.support24.position.service;

import lv.dsns.support24.position.controller.dto.request.PositionRequestDTO;
import lv.dsns.support24.position.controller.dto.response.PositionResponseDTO;

import java.util.List;

public interface PositionService {

    PositionResponseDTO save(PositionRequestDTO positionRequestDTO);

    boolean existsByPositionName(String positionName);

    List<PositionResponseDTO> findAll();
}
