package lv.dsns.support24.unit.service.imp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.dsns.support24.common.dto.response.PageResponse;
import lv.dsns.support24.common.exception.ClientBackendException;
import lv.dsns.support24.common.exception.ErrorCode;
import lv.dsns.support24.unit.controller.dto.enums.UnitStatus;
import lv.dsns.support24.unit.controller.dto.enums.UnitType;
import lv.dsns.support24.unit.controller.dto.request.UnitRequestDTO;
import lv.dsns.support24.unit.controller.dto.response.UnitResponseDTO;
import lv.dsns.support24.unit.mapper.UnitMapper;
import lv.dsns.support24.unit.repository.UnitRepository;
import lv.dsns.support24.unit.repository.entity.Unit;
import lv.dsns.support24.unit.service.UnitService;
import lv.dsns.support24.unit.service.filter.UnitFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public PageResponse<UnitResponseDTO> findAllPageable(UnitFilter unitFilter, Pageable pageable){
        Page<Unit> allUnits = unitRepository.findAll(getSearchSpecification(unitFilter), pageable);

        List<UnitResponseDTO> unitResponseDto = allUnits.stream()
                .map(unitMapper::mapToDTO)
                .collect(Collectors.toList());
        return PageResponse.<UnitResponseDTO>builder()
                .totalPages((long) allUnits.getTotalPages())
                .pageSize((long) pageable.getPageSize())
                .totalElements(allUnits.getTotalElements())
                .content(unitResponseDto)
                .build();
    }
    @Override
    public List<UnitResponseDTO> findAll(){
        var allTasks = unitRepository.findAll();
        return allTasks.stream().map(unitMapper::mapToDTO)
                .collect(Collectors.toList());
    }
    @Override
    public List<UnitResponseDTO> findAllChildUnits(UUID id){
        var allChild = unitRepository.findHierarchyByUnitId(id);

        return allChild.stream()
                .map(unitMapper::mapToDTO)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public UnitResponseDTO patchUnit(UUID id, UnitRequestDTO requestDTO){
        var unitById = unitRepository.findById(id)
                .orElseThrow(() -> new ClientBackendException(ErrorCode.UNIT_NOT_FOUND));
        unitMapper.patchMerge(requestDTO, unitById);
        return unitMapper.mapToDTO(unitById);
    }
    @Override
    public void delete(UUID id){
        var unitById = unitRepository.findById(id)
                .orElseThrow(() -> new ClientBackendException(ErrorCode.UNIT_NOT_FOUND));

        unitById.setUnitStatus(UnitStatus.DELETED);

        unitRepository.save(unitById);
    }
    @Override
    public boolean existUnitByUnitName(String unitName){
        return unitRepository.existsUnitsByUnitName(unitName);
    }

    private Specification<Unit> getSearchSpecification(UnitFilter unitFilter) {
        return Specification.where((Specification<Unit>) searchOnField("unitType", unitFilter.getUnitType()))
                .and((Specification<Unit>) searchLikeString("unitName", unitFilter.getUnitName()))
                .and((Specification<Unit>) searchOnField("unitStatus", unitFilter.getStatuses()))
                .and((Specification<Unit>) searchFieldInCollectionOfIds("id", unitFilter.getUnitId()));
    }

    @Override
    public List<UnitResponseDTO> findAllByUnitType(UnitType unitType){
        var allByUnitType = unitRepository.findAllByUnitType(unitType);
        return allByUnitType.stream().map(unitMapper::mapToDTO).collect(Collectors.toList());
    }

}
