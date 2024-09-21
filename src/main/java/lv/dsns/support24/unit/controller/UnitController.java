package lv.dsns.support24.unit.controller;

import lv.dsns.support24.task.controller.dto.request.TaskRequestDTO;
import lv.dsns.support24.task.controller.dto.response.TaskResponseDTO;
import lv.dsns.support24.task.service.filter.TaskFilter;
import lv.dsns.support24.unit.controller.dto.request.UnitRequestDTO;
import lv.dsns.support24.unit.controller.dto.response.UnitResponseDTO;
import lv.dsns.support24.unit.service.UnitService;
import lv.dsns.support24.unit.service.filter.UnitFilter;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/public/units")
    public ResponseEntity<List<UnitResponseDTO>> getAllUnits(@ParameterObject UnitFilter unitFilter){
        var allUnits = unitService.findAll(unitFilter);
        return ResponseEntity.ok(allUnits);
    }

    @GetMapping("/public/units/child-units/{id}")
    public ResponseEntity<List<UnitResponseDTO>> getChildUnits(@PathVariable UUID id){
        var allChildUnits = unitService.findAllChildUnits(id);
        return ResponseEntity.ok(allChildUnits);
    }

    @PatchMapping("/public/units/{id}")
    public ResponseEntity<UnitResponseDTO> patch(@PathVariable UUID id, @RequestBody UnitRequestDTO requestDTO){
        var patchedTask = unitService.patchUnit(id,requestDTO);
        return ResponseEntity.ok(patchedTask);
    }

    @DeleteMapping("/public/units/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        unitService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
