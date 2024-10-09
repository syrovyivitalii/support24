package lv.dsns.support24.nabatgroup.controller;

import lv.dsns.support24.nabatgroup.controller.dto.request.NabatGroupRequestDTO;
import lv.dsns.support24.nabatgroup.controller.dto.response.NabatGroupResponseDTO;
import lv.dsns.support24.nabatgroup.service.NabatGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/private/nabat/group")
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
}
