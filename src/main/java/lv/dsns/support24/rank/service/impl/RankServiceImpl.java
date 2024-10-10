package lv.dsns.support24.rank.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.dsns.support24.rank.controller.dto.request.RankRequestDTO;
import lv.dsns.support24.rank.controller.dto.response.RankResponseDTO;
import lv.dsns.support24.rank.mapper.RankMapper;
import lv.dsns.support24.rank.repository.RankRepository;
import lv.dsns.support24.rank.repository.entity.Rank;
import lv.dsns.support24.rank.service.RankService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RankServiceImpl implements RankService {
    private final RankMapper rankMapper;
    private final RankRepository rankRepository;

    @Override
    public RankResponseDTO save(RankRequestDTO rankRequestDTO) {
        var rank = rankMapper.mapToEntity(rankRequestDTO);
        rankRepository.save(rank);
        return rankMapper.mapToDTO(rank);
    }

    @Override
    public boolean existsByRankName(String rankName) {
        return rankRepository.existsByRankName(rankName);
    }

    @Override
    public List<RankResponseDTO> findAll() {
        var allRanks = rankRepository.findAll(Sort.by(Sort.Direction.ASC, "sortBy"));
        return allRanks.stream().map(rankMapper :: mapToDTO).collect(Collectors.toList());
    }

}
