package lv.dsns.support24.problem.mapper;

import lv.dsns.support24.problem.controller.dto.request.ProblemRequestDTO;
import lv.dsns.support24.problem.controller.dto.response.ProblemResponseDTO;
import lv.dsns.support24.problem.repository.entity.Problem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProblemMapper {
    Problem mapToEntity(ProblemRequestDTO problemRequestDTO);

    ProblemResponseDTO mapToDTO (Problem problem);
}
