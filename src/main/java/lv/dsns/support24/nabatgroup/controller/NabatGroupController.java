package lv.dsns.support24.nabatgroup.controller;

import lv.dsns.support24.nabatgroup.controller.dto.request.NabatGroupRequestDTO;
import lv.dsns.support24.nabatgroup.controller.dto.response.NabatGroupResponseDTO;
import lv.dsns.support24.nabatgroup.service.NabatGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/private/nabat/groups")
public class NabatGroupController {
    private final NabatGroupService nabatGroupService;

    public NabatGroupController(NabatGroupService nabatGroupService) {
        this.nabatGroupService = nabatGroupService;
    }

    @PostMapping
    private ResponseEntity<NabatGroupResponseDTO> save(@RequestBody NabatGroupRequestDTO nabatGroupRequestDTO) {
        var savedNabatGroup = nabatGroupService.save(nabatGroupRequestDTO);
        return ResponseEntity.ok(savedNabatGroup);
    }

    @GetMapping
    private ResponseEntity<List<NabatGroupResponseDTO>> findAll() {
        var allNabatGroups = nabatGroupService.findAll();
        return ResponseEntity.ok(allNabatGroups);
    }

    @GetMapping("/by-unit/{unitId}")
    private ResponseEntity<List<NabatGroupResponseDTO>> findByUnitId(@PathVariable UUID unitId) {
        var allByUnitId = nabatGroupService.findAllByUnitId(unitId);
        return ResponseEntity.ok(allByUnitId);
    }
}
