package lv.dsns.support24.unit.controller;

import lv.dsns.support24.task.service.filter.TaskFilter;
import lv.dsns.support24.unit.controller.dto.response.UnitResponseDTO;
import lv.dsns.support24.unit.service.UnitService;
import lv.dsns.support24.unit.service.filter.UnitFilter;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UnitController {

    private final UnitService unitService;

    public UnitController(UnitService unitService) {
        this.unitService = unitService;
    }

    @GetMapping("/public/units")
    public ResponseEntity<List<UnitResponseDTO>> getAllTasks(@ParameterObject UnitFilter unitFilter){
        var allUnits = unitService.findAll(unitFilter);
        return ResponseEntity.ok(allUnits);
    }

}
