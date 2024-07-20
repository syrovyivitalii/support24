package lv.dsns.support24.problems.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.dsns.support24.problems.controller.dto.request.ProblemRequestDTO;
import lv.dsns.support24.problems.controller.dto.response.ProblemResponseDTO;
import lv.dsns.support24.problems.mapper.ProblemMapper;
import lv.dsns.support24.problems.repository.ProblemRepository;
import lv.dsns.support24.problems.service.ProblemService;
import lv.dsns.support24.unit.controller.dto.response.UnitResponseDTO;
import lv.dsns.support24.unit.service.filter.UnitFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        var allTasks = problemRepository.findAll();
        return allTasks.stream().map(problemMapper::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public boolean existByProblem(String problem){
        return problemRepository.existsByProblem(problem);
    }
}
