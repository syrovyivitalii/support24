package lv.dsns.support24.nabatgroup.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.dsns.support24.nabatgroup.controller.dto.request.NabatGroupRequestDTO;
import lv.dsns.support24.nabatgroup.controller.dto.response.NabatGroupResponseDTO;
import lv.dsns.support24.nabatgroup.mapper.NabatGroupMapper;
import lv.dsns.support24.nabatgroup.repository.NabatGroupRepository;
import lv.dsns.support24.nabatgroup.service.NabatGroupService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NabatGroupServiceImpl implements NabatGroupService {
    private final NabatGroupMapper nabatGroupMapper;
    private final NabatGroupRepository nabatGroupRepository;

    @Override
    public NabatGroupResponseDTO save(NabatGroupRequestDTO nabatGroupRequestDTO) {
        var nabatGroup = nabatGroupMapper.mapToEntity(nabatGroupRequestDTO);
        nabatGroupRepository.save(nabatGroup);
        return nabatGroupMapper.mapToDTO(nabatGroup);
    }
}
