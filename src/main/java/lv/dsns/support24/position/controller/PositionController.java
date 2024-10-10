package lv.dsns.support24.position.controller;

import lv.dsns.support24.position.controller.dto.response.PositionResponseDTO;
import lv.dsns.support24.position.service.PositionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/private/positions")
public class PositionController {
    private final PositionService positionService;

    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @GetMapping
    public ResponseEntity<List<PositionResponseDTO>> findAllPositions() {
        var allPositions = positionService.findAll();
        return ResponseEntity.ok(allPositions);
    }

}