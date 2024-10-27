package lv.dsns.support24.nabat.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.dsns.support24.common.dto.response.PageResponse;
import lv.dsns.support24.common.exception.ClientBackendException;
import lv.dsns.support24.common.exception.ErrorCode;
import lv.dsns.support24.nabat.controller.dto.request.NabatRequestDTO;
import lv.dsns.support24.nabat.controller.dto.response.NabatResponseDTO;
import lv.dsns.support24.nabat.mapper.NabatMapper;
import lv.dsns.support24.nabat.repository.NabatRepository;
import lv.dsns.support24.nabat.repository.entity.Nabat;
import lv.dsns.support24.nabat.service.NabatService;
import lv.dsns.support24.nabatgroup.repository.NabatGroupRepository;
import lv.dsns.support24.notify.dto.response.NotifyResponseDTO;
import lv.dsns.support24.notify.NotifyClient;
import lv.dsns.support24.notify.dto.request.NotifyRequestDTO;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class NabatServiceImpl implements NabatService {

    private final NabatRepository nabatRepository;
    private final NabatGroupRepository nabatGroupRepository;
    private final NabatMapper nabatMapper;
    private final NotifyClient notifyClient;

    @Override
    public NabatResponseDTO save(NabatRequestDTO nabatRequestDTO) {

        if(nabatRepository.existsByUserIdAndNabatGroupId(nabatRequestDTO.getUserId(), nabatRequestDTO.getNabatGroupId())){
            throw new ClientBackendException(ErrorCode.USER_ALREADY_EXISTS);
        }

        var nabatItem = nabatMapper.mapToEntity(nabatRequestDTO);
        nabatRepository.save(nabatItem);

        return nabatMapper.mapToDTO(nabatItem);
    }

    @Override
    @Transactional
    public List<NabatResponseDTO> saveList(Set<NabatRequestDTO> nabatRequestDTOs) {
        List<NabatResponseDTO> savedNabatItems = new ArrayList<>();

        // Validate upfront: Check if any DTOs already exist in the repository
        for (NabatRequestDTO nabatRequestDTO : nabatRequestDTOs) {
            if (nabatRepository.existsByUserIdAndNabatGroupId(nabatRequestDTO.getUserId(), nabatRequestDTO.getNabatGroupId())) {
                throw new ClientBackendException(ErrorCode.USER_ALREADY_EXISTS);
            }
        }

        // If validation passes, proceed with the save operation
        for (NabatRequestDTO nabatRequestDTO : nabatRequestDTOs) {
            var nabatItem = nabatMapper.mapToEntity(nabatRequestDTO);
            nabatItem = nabatRepository.save(nabatItem);  // Persist entity
            savedNabatItems.add(nabatMapper.mapToDTO(nabatItem));  // Map entity to DTO and add to result list
        }

        return savedNabatItems;
    }

    @Override
    public List<NabatResponseDTO> getAll() {
        var all = nabatRepository.findAll();

        return all.stream().map(nabatMapper::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public PageResponse<NabatResponseDTO> getAllByNabatGroup(UUID nabatGroupId, Pageable pageable) {
        Page<Nabat> allNabats = nabatRepository.findByNabatGroupId(nabatGroupId, pageable);

        List<NabatResponseDTO> nabatResponseDTOS = allNabats.stream()
                .map(nabatMapper::mapToDTO)
                .collect(Collectors.toList());
        return PageResponse.<NabatResponseDTO>builder()
                .totalPages((long) allNabats.getTotalPages())
                .pageSize((long) pageable.getPageSize())
                .totalElements(allNabats.getTotalElements())
                .content(nabatResponseDTOS)
                .build();
    }

    @Override
    public void delete(UUID nabatGroupId, UUID userId) {
        var byUserIdAndNabatGroupId = nabatRepository.findByUserIdAndNabatGroupId(userId, nabatGroupId)
                .orElseThrow(() -> new ClientBackendException(ErrorCode.USER_NOT_FOUND));

        nabatRepository.delete(byUserIdAndNabatGroupId);
    }

    @Override
    public NotifyResponseDTO nabatNotify(UUID nabatGroupId, NotifyRequestDTO message) {

        boolean exists = nabatGroupRepository.existsById(nabatGroupId);

        if (BooleanUtils.isFalse(exists)) {
            throw new ClientBackendException(ErrorCode.NABAT_GROUP_NOT_FOUND);
        }

        List<UUID> userIds = nabatRepository.findUserIdByNabatGroupId(nabatGroupId);

        List<Object[]> usersToNotify = nabatRepository.findPhonesByUserIds(userIds);

        try {
            return notifyClient.notifyUsers(usersToNotify, message);
        } catch (IOException e) {
            throw new ClientBackendException(ErrorCode.NOTIFICATION_FAILED);
        }
    }
}
