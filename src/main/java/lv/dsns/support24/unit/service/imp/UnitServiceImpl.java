package lv.dsns.support24.unit.service.imp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.dsns.support24.unit.controller.dto.request.UnitRequestDTO;
import lv.dsns.support24.unit.controller.dto.response.UnitResponseDTO;
import lv.dsns.support24.unit.mapper.UnitMapper;
import lv.dsns.support24.unit.repository.UnitRepository;
import lv.dsns.support24.unit.service.UnitService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UnitServiceImpl implements UnitService {

    private final UnitMapper unitMapper;
    private final UnitRepository unitRepository;

    @Override
    @Transactional
    public UnitResponseDTO save (UnitRequestDTO unitRequestDTO){
        var unit = unitMapper.mapToEntity(unitRequestDTO);

        var savedTask = unitRepository.save(unit);
        return unitMapper.mapToDTO(savedTask);
    }

    @Override
    public boolean existUnitByUnitName(String unitName){
        return unitRepository.existsUnitsByUnitName(unitName);
    }

}
