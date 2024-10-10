package lv.dsns.support24.position.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.dsns.support24.position.controller.dto.request.PositionRequestDTO;
import lv.dsns.support24.position.controller.dto.response.PositionResponseDTO;
import lv.dsns.support24.position.mapper.PositionMapper;
import lv.dsns.support24.position.repository.PositionRepository;
import lv.dsns.support24.position.service.PositionService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PositionServiceImpl implements PositionService {

    private final PositionMapper positionMapper;
    private final PositionRepository positionRepository;

    @Override
    public PositionResponseDTO save(PositionRequestDTO positionRequestDTO) {
        var position = positionMapper.mapToEntity(positionRequestDTO);
        positionRepository.save(position);
        return positionMapper.mapToDTO(position);
    }

    @Override
    public boolean existsByPositionName(String positionName) {
        return positionRepository.existsByPositionName(positionName);
    }
}
