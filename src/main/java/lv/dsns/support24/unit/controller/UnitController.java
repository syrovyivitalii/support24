package lv.dsns.support24.unit.controller;

import io.swagger.v3.oas.annotations.Operation;
import lv.dsns.support24.common.dto.response.PageResponse;
import lv.dsns.support24.task.controller.dto.request.TaskRequestDTO;
import lv.dsns.support24.task.controller.dto.response.TaskResponseDTO;
import lv.dsns.support24.task.service.filter.TaskFilter;
import lv.dsns.support24.unit.controller.dto.request.UnitRequestDTO;
import lv.dsns.support24.unit.controller.dto.response.UnitResponseDTO;
import lv.dsns.support24.unit.service.UnitService;
import lv.dsns.support24.unit.service.filter.UnitFilter;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class UnitController {

    private final UnitService unitService;

    public UnitController(UnitService unitService) {
        this.unitService = unitService;
    }

    @GetMapping("/private/units/pageable")
    public ResponseEntity<PageResponse<UnitResponseDTO>> getAllUnitsPageable(@ParameterObject UnitFilter unitFilter, @SortDefault(sort = "unitType", direction = Sort.Direction.ASC) @ParameterObject Pageable pageable){
        PageResponse <UnitResponseDTO> responseDTOs = unitService.findAllPageable(unitFilter, pageable);
        return ResponseEntity.ok(responseDTOs);
    }
    @GetMapping("/public/units")
    public ResponseEntity<List<UnitResponseDTO>> getAllUnits(){
        var allUnits = unitService.findAll();
        return ResponseEntity.ok(allUnits);
    }

    @GetMapping("/private/units/child-units/{id}")
    public ResponseEntity<PageResponse<UnitResponseDTO>> getChildUnits(@PathVariable UUID id, @ParameterObject Pageable pageable){
        PageResponse<UnitResponseDTO> unitResponseDto = unitService.findAllChildUnits(id, pageable);
        return ResponseEntity.ok(unitResponseDto);
    }

    @PatchMapping("/private/units/{id}")
    @PreAuthorize("hasRole('ROLE_SYSTEM_ADMIN')")
    @Operation(summary = "Accessible by ROLE_SYSTEM_ADMIN")
    public ResponseEntity<UnitResponseDTO> patch(@PathVariable UUID id, @RequestBody UnitRequestDTO requestDTO){
        var patchedTask = unitService.patchUnit(id,requestDTO);
        return ResponseEntity.ok(patchedTask);
    }

    @DeleteMapping("/private/units/{id}")
    @PreAuthorize("hasRole('ROLE_SYSTEM_ADMIN')")
    @Operation(summary = "Accessible by ROLE_SYSTEM_ADMIN")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        unitService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
