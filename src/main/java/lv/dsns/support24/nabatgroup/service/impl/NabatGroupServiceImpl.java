package lv.dsns.support24.nabatgroup.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.dsns.support24.common.exception.ClientBackendException;
import lv.dsns.support24.common.exception.ErrorCode;
import lv.dsns.support24.nabatgroup.controller.dto.request.NabatGroupRequestDTO;
import lv.dsns.support24.nabatgroup.controller.dto.response.NabatGroupResponseDTO;
import lv.dsns.support24.nabatgroup.mapper.NabatGroupMapper;
import lv.dsns.support24.nabatgroup.repository.NabatGroupRepository;
import lv.dsns.support24.nabatgroup.repository.entity.NabatGroup;
import lv.dsns.support24.nabatgroup.service.NabatGroupService;
import lv.dsns.support24.user.repository.SystemUsersRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NabatGroupServiceImpl implements NabatGroupService {
    private final NabatGroupMapper nabatGroupMapper;
    private final NabatGroupRepository nabatGroupRepository;
    private final SystemUsersRepository usersRepository;

    @Override
    public NabatGroupResponseDTO save(Principal principal, NabatGroupRequestDTO nabatGroupRequestDTO) {
        var authUser = usersRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ClientBackendException(ErrorCode.USER_NOT_FOUND));
        var nabatGroup = nabatGroupMapper.mapToEntity(nabatGroupRequestDTO);

        if (nabatGroupRepository.existsByGroupNameAndUnitId(nabatGroup.getGroupName(), authUser.getUnitId())) {
            throw new ClientBackendException(ErrorCode.GROUP_ALREADY_EXISTS);
        }

        nabatGroupRepository.save(nabatGroup);
        return nabatGroupMapper.mapToDTO(nabatGroup);
    }

    @Override
    public List<NabatGroupResponseDTO> findAll() {
        var all = nabatGroupRepository.findAll(Sort.by(Sort.Direction.ASC, "groupName"));
        return all.stream().map(nabatGroupMapper ::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public List<NabatGroupResponseDTO> findAllByUnitId(UUID unitId) {
        var allByUnitId = nabatGroupRepository.findByUnitId(unitId);
        return allByUnitId.stream().map(nabatGroupMapper ::mapToDTO).collect(Collectors.toList());
    }
}
