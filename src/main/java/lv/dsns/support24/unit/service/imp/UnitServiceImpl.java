package lv.dsns.support24.unit.service.imp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.dsns.support24.common.exception.ClientBackendException;
import lv.dsns.support24.common.exception.ErrorCode;
import lv.dsns.support24.unit.controller.dto.enums.UnitType;
import lv.dsns.support24.unit.controller.dto.request.UnitRequestDTO;
import lv.dsns.support24.unit.controller.dto.response.UnitResponseDTO;
import lv.dsns.support24.unit.mapper.UnitMapper;
import lv.dsns.support24.unit.repository.UnitRepository;
import lv.dsns.support24.unit.repository.entity.Unit;
import lv.dsns.support24.unit.service.UnitService;
import lv.dsns.support24.unit.service.filter.UnitFilter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
    public List<UnitResponseDTO> findAllChildUnits(UUID id){
        var parentUnit = unitRepository.findById(id)
                .orElseThrow(() -> new ClientBackendException(ErrorCode.UNIT_NOT_FOUND));

        if (parentUnit.getUnitType().equals(UnitType.ГУ)){
            List<Unit> allChild = unitRepository.findByParentUnitIdNotNull();
            return allChild.stream().map(unitMapper::mapToDTO).collect(Collectors.toList());
        }else {
            List<Unit> allChild = unitRepository.findByParentUnitIdAndGroupId(parentUnit.getId(), parentUnit.getGroupId());
            return allChild.stream().map(unitMapper::mapToDTO).collect(Collectors.toList());
        }
    }

    @Override
    public boolean existUnitByUnitName(String unitName){
        return unitRepository.existsUnitsByUnitName(unitName);
    }

    private Specification<Unit> getSearchSpecification(UnitFilter unitFilter) {
        return Specification.where((Specification<Unit>) searchOnUnitType(unitFilter.getUnitType()));
    }

}
