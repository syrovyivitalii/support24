package lv.dsns.support24.problems.service;

import lv.dsns.support24.problems.controller.dto.request.ProblemRequestDTO;
import lv.dsns.support24.problems.controller.dto.response.ProblemResponseDTO;

import java.util.List;
import java.util.UUID;

public interface ProblemService {
    ProblemResponseDTO save (ProblemRequestDTO problemRequestDTO);
    List<ProblemResponseDTO> findAll();
    void delete(UUID id);
    boolean existByProblem(String problem);
}
