package lv.dsns.support24.nabat.controller;

import lv.dsns.support24.common.dto.response.PageResponse;
import lv.dsns.support24.nabat.controller.dto.request.NabatRequestDTO;
import lv.dsns.support24.nabat.controller.dto.response.NabatResponseDTO;
import lv.dsns.support24.nabat.service.NabatService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/private/nabat")
public class NabatController {
    private final NabatService nabatService;

    public NabatController(NabatService nabatService) {
        this.nabatService = nabatService;
    }

    @PostMapping
    public ResponseEntity<NabatResponseDTO> save (@RequestBody NabatRequestDTO nabatRequestDTO) {
        var savedNabatItem = nabatService.save(nabatRequestDTO);

        return ResponseEntity.ok(savedNabatItem);
    }

    @PostMapping("/list")
    public ResponseEntity<List<NabatResponseDTO>> saveList (@RequestBody Set<NabatRequestDTO> nabatRequestDTOList) {
        var savedNabatItem = nabatService.saveList(nabatRequestDTOList);

        return ResponseEntity.ok(savedNabatItem);
    }

    @GetMapping
    public ResponseEntity<List<NabatResponseDTO>> findAll() {
        var all = nabatService.getAll();

        return ResponseEntity.ok(all);
    }

    @GetMapping("/by-nabat-group/{nabatGroupId}/pageable")
    public ResponseEntity<PageResponse<NabatResponseDTO>> findByNabatGroupId(@PathVariable UUID nabatGroupId, @ParameterObject Pageable pageable) {
        var allByNabatGroup = nabatService.getAllByNabatGroup(nabatGroupId, pageable);

        return ResponseEntity.ok(allByNabatGroup);
    }

    @DeleteMapping("/by-nabat-group/{nabatGroupId}/by-user/{userId}")
    public ResponseEntity<Void> delete (@PathVariable UUID nabatGroupId, @PathVariable UUID userId) {
        nabatService.delete(nabatGroupId, userId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
