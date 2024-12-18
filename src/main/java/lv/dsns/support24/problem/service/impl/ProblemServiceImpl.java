package lv.dsns.support24.problem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.dsns.support24.common.exception.ClientBackendException;
import lv.dsns.support24.common.exception.ErrorCode;
import lv.dsns.support24.problem.controller.dto.request.ProblemRequestDTO;
import lv.dsns.support24.problem.controller.dto.response.ProblemResponseDTO;
import lv.dsns.support24.problem.mapper.ProblemMapper;
import lv.dsns.support24.problem.repository.ProblemRepository;
import lv.dsns.support24.problem.service.ProblemService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProblemServiceImpl implements ProblemService {

    private final ProblemMapper problemMapper;
    private final ProblemRepository problemRepository;

    @Override
    @Transactional
    public ProblemResponseDTO save (ProblemRequestDTO problemRequestDTO){
        var problem = problemMapper.mapToEntity(problemRequestDTO);
        var savedTask = problemRepository.save(problem);

        return problemMapper.mapToDTO(savedTask);
    }

    @Override
    public List<ProblemResponseDTO> findAll(){
        var allTasks = problemRepository.findAll(Sort.by(Sort.Direction.ASC, "problem"));

        return allTasks.stream().map(problemMapper::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id){
        var problemById = problemRepository.findById(id)
                .orElseThrow(() -> new ClientBackendException(ErrorCode.PROBLEM_NOT_FOUND));

        problemRepository.deleteById(problemById.getId());
    }

    @Override
    public boolean existByProblem(String problem){
        return problemRepository.existsByProblem(problem);
    }
}
