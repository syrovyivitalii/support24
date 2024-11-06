package lv.dsns.support24.nabatgroup.controller;

import lombok.RequiredArgsConstructor;
import lv.dsns.support24.nabatgroup.controller.dto.request.NabatGroupRequestDTO;
import lv.dsns.support24.nabatgroup.controller.dto.response.NabatGroupResponseDTO;
import lv.dsns.support24.nabatgroup.service.NabatGroupService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/private/nabat/groups")
@RequiredArgsConstructor
public class NabatGroupController {

    private final NabatGroupService nabatGroupService;

    @PostMapping
    private ResponseEntity<NabatGroupResponseDTO> save(@ParameterObject Principal principal, @RequestBody NabatGroupRequestDTO nabatGroupRequestDTO) {
        var savedNabatGroup = nabatGroupService.save(principal, nabatGroupRequestDTO);

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
    @GetMapping("/{groupId}")
    private ResponseEntity<NabatGroupResponseDTO> findById(@PathVariable UUID groupId) {
        var nabatGroup = nabatGroupService.findById(groupId);

        return ResponseEntity.ok(nabatGroup);
    }

    @PatchMapping("/{groupId}")
    private ResponseEntity<NabatGroupResponseDTO> patch (@PathVariable UUID groupId, @RequestBody NabatGroupRequestDTO nabatGroupRequestDTO) {
        var updated = nabatGroupService.update(groupId, nabatGroupRequestDTO);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{groupId}")
    private ResponseEntity<Void> delete (@PathVariable UUID groupId) {
        nabatGroupService.delete(groupId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
