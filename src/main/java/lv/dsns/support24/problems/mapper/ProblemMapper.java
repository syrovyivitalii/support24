package lv.dsns.support24.problems.mapper;

import lv.dsns.support24.problems.controller.dto.request.ProblemRequestDTO;
import lv.dsns.support24.problems.controller.dto.response.ProblemResponseDTO;
import lv.dsns.support24.problems.repository.entity.Problems;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProblemMapper {
    Problems mapToEntity(ProblemRequestDTO problemRequestDTO);

    ProblemResponseDTO mapToDTO (Problems problems);
}
