package lv.dsns.support24.unit.service.imp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.dsns.support24.task.controller.dto.response.TaskResponseDTO;
import lv.dsns.support24.task.repository.entity.Tasks;
import lv.dsns.support24.task.service.filter.TaskFilter;
import lv.dsns.support24.unit.controller.dto.request.UnitRequestDTO;
import lv.dsns.support24.unit.controller.dto.response.UnitResponseDTO;
import lv.dsns.support24.unit.mapper.UnitMapper;
import lv.dsns.support24.unit.repository.UnitRepository;
import lv.dsns.support24.unit.repository.entity.Units;
import lv.dsns.support24.unit.service.UnitService;
import lv.dsns.support24.unit.service.filter.UnitFilter;
import lv.dsns.support24.user.controller.dto.response.UserResponseDTO;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static lv.dsns.support24.common.specification.SpecificationCustom.*;

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
    public List<UnitResponseDTO> findAll(UnitFilter unitFilter){
        var allTasks = unitRepository.findAll(getSearchSpecification(unitFilter));
        return allTasks.stream().map(unitMapper::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public boolean existUnitByUnitName(String unitName){
        return unitRepository.existsUnitsByUnitName(unitName);
    }

    private Specification<Units> getSearchSpecification(UnitFilter unitFilter) {
        return Specification.where((Specification<Units>) searchOnUnitType(unitFilter.getUnitType()));
    }

}
