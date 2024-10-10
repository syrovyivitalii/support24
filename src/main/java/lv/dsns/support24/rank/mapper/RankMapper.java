package lv.dsns.support24.rank.mapper;

import lv.dsns.support24.rank.controller.dto.request.RankRequestDTO;
import lv.dsns.support24.rank.controller.dto.response.RankResponseDTO;
import lv.dsns.support24.rank.repository.entity.Rank;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RankMapper {
    Rank mapToEntity(RankRequestDTO rankRequestDTO);

    RankResponseDTO mapToDTO(Rank rank);
}
