package lv.dsns.support24.rank.controller;

import lv.dsns.support24.rank.controller.dto.request.RankRequestDTO;
import lv.dsns.support24.rank.controller.dto.response.RankResponseDTO;
import lv.dsns.support24.rank.service.RankService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/private/rank")
public class RankController {
    private final RankService rankService;

    public RankController(RankService rankService) {
        this.rankService = rankService;
    }

    @GetMapping
    private ResponseEntity<RankResponseDTO> save (@RequestBody RankRequestDTO rankRequestDTO) {
        var result = rankService.save(rankRequestDTO);
        return ResponseEntity.ok(result);
    }
}
