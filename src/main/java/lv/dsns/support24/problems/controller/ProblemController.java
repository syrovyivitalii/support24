package lv.dsns.support24.problems.controller;

import lv.dsns.support24.problems.controller.dto.request.ProblemRequestDTO;
import lv.dsns.support24.problems.controller.dto.response.ProblemResponseDTO;
import lv.dsns.support24.problems.service.ProblemService;
import lv.dsns.support24.user.controller.dto.request.UserRequestDTO;
import lv.dsns.support24.user.controller.dto.response.UserResponseDTO;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/private/common-problems")
public class ProblemController {

    private final ProblemService problemService;

    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @GetMapping()
    public ResponseEntity<List<ProblemResponseDTO>> getAll(){
        var allProblems = problemService.findAll();
        return ResponseEntity.ok(allProblems);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        problemService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PostMapping
    public ResponseEntity<ProblemResponseDTO> save (@RequestBody ProblemRequestDTO problemRequestDTO){
        var responseDto = problemService.save(problemRequestDTO);
        return ResponseEntity.ok(responseDto);
    }
}
