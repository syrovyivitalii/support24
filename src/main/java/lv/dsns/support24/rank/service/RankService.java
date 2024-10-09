package lv.dsns.support24.rank.service;

import lv.dsns.support24.rank.controller.dto.request.RankRequestDTO;
import lv.dsns.support24.rank.controller.dto.response.RankResponseDTO;

public interface RankService {
    RankResponseDTO save(RankRequestDTO rankRequestDTO);
    boolean existsByRankName(String rankName);
}
