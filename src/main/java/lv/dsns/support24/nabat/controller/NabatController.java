package lv.dsns.support24.nabat.controller;

import lv.dsns.support24.nabat.controller.dto.request.NabatRequestDTO;
import lv.dsns.support24.nabat.controller.dto.response.NabatResponseDTO;
import lv.dsns.support24.nabat.service.NabatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping
    public ResponseEntity<List<NabatResponseDTO>> findAll() {
        var all = nabatService.getAll();

        return ResponseEntity.ok(all);
    }

    @GetMapping("/by-nabat-group/{nabatGroupId}")
    public ResponseEntity<List<NabatResponseDTO>> findByNabatGroupId(@PathVariable UUID nabatGroupId) {
        var allByNabatGroup = nabatService.getAllByNabatGroup(nabatGroupId);

        return ResponseEntity.ok(allByNabatGroup);
    }

    @DeleteMapping("/by-nabat-group{nabatGroupId}/by-user/{userId}")
    public ResponseEntity<Void> delete (@PathVariable UUID nabatGroupId, @PathVariable UUID userId) {
        nabatService.delete(nabatGroupId, userId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
