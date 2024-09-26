package lv.dsns.support24.problem.service;

import lv.dsns.support24.problem.controller.dto.request.ProblemRequestDTO;
import lv.dsns.support24.problem.controller.dto.response.ProblemResponseDTO;

import java.util.List;
import java.util.UUID;

public interface ProblemService {
    ProblemResponseDTO save (ProblemRequestDTO problemRequestDTO);
    List<ProblemResponseDTO> findAll();
    void delete(UUID id);
    boolean existByProblem(String problem);
}
