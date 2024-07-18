package lv.dsns.support24.problems.service;

import lv.dsns.support24.problems.controller.dto.request.ProblemRequestDTO;
import lv.dsns.support24.problems.controller.dto.response.ProblemResponseDTO;

public interface ProblemService {
    ProblemResponseDTO save (ProblemRequestDTO problemRequestDTO);
    boolean existByProblem(String problem);
}
