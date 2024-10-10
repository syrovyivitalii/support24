package lv.dsns.support24.position.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.dsns.support24.position.controller.dto.request.PositionRequestDTO;
import lv.dsns.support24.position.controller.dto.response.PositionResponseDTO;
import lv.dsns.support24.position.mapper.PositionMapper;
import lv.dsns.support24.position.repository.PositionRepository;
import lv.dsns.support24.position.repository.entity.Position;
import lv.dsns.support24.position.service.PositionService;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<PositionResponseDTO> findAll() {
        var allPositions = positionRepository.findAll(Sort.by(Sort.Direction.ASC, "positionName"));

        return allPositions.stream()
                .map(positionMapper::mapToDTO)
                .collect(Collectors.toList());
    }
}
