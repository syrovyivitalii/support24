package lv.dsns.support24.problems.controller;

import lv.dsns.support24.problems.controller.dto.response.ProblemResponseDTO;
import lv.dsns.support24.problems.service.ProblemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/private/common-problems")
public class ProblemController {

    private final ProblemService problemService;

    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @GetMapping()
    public ResponseEntity<List<ProblemResponseDTO>> getAllTasks(){
        var allProblems = problemService.findAll();
        return ResponseEntity.ok(allProblems);
    }
}
