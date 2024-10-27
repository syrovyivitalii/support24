package lv.dsns.support24.nabat.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import lv.dsns.support24.notificationlog.controller.dto.request.NotificationLogRequestDTO;
import lv.dsns.support24.notificationlog.controller.dto.response.NotificationLogResponseDTO;
import lv.dsns.support24.notificationlog.repository.NotificationLogRepository;
import lv.dsns.support24.notificationlog.repository.entity.NotificationLog;
import lv.dsns.support24.notificationlog.service.NotificationLogService;
import lv.dsns.support24.notify.dto.response.NotifyResponseDTO;
import lv.dsns.support24.notify.client.NotifyClient;
import lv.dsns.support24.notify.dto.request.NotifyRequestDTO;
import lv.dsns.support24.notifyresult.client.NotifyResultClient;
import lv.dsns.support24.notifyresult.model.NotifyResult;
import lv.dsns.support24.user.repository.SystemUsersRepository;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class NabatServiceImpl implements NabatService {

    private final NabatRepository nabatRepository;
    private final NabatGroupRepository nabatGroupRepository;
    private final NotificationLogService notificationLogService;
    private final NotificationLogRepository notificationLogRepository;
    private final SystemUsersRepository usersRepository;
    private final NabatMapper nabatMapper;
    private final NotifyClient notifyClient;
    private final NotifyResultClient notifyResultClient;
    private final ObjectMapper mapper;

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
    public NotifyResponseDTO nabatNotify(UUID nabatGroupId, NotifyRequestDTO requestDTO, Principal principal) {

        nabatGroupRepository.findById(nabatGroupId).orElseThrow(() -> new ClientBackendException(ErrorCode.NABAT_GROUP_NOT_FOUND));

        var userByEmail = usersRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ClientBackendException(ErrorCode.USER_NOT_FOUND));

        List<UUID> userIds = nabatRepository.findUserIdByNabatGroupId(nabatGroupId);
        if (userIds.isEmpty()) {
            throw new ClientBackendException(ErrorCode.GROUP_IS_EMPTY);
        }

        List<Object[]> usersToNotify = nabatRepository.findPhonesByUserIds(userIds);
        if (usersToNotify.isEmpty()) {
            throw new ClientBackendException(ErrorCode.NO_PHONES_FOUND);
        }

        UUID eventId = null;

        try {
            NotifyResponseDTO notifyResponseDTO = notifyClient.notifyUsers(usersToNotify, requestDTO);

            eventId = notifyResponseDTO.getEventId();

            return notifyResponseDTO;
        } catch (IOException e) {
            throw new ClientBackendException(ErrorCode.NOTIFICATION_FAILED);
        }finally {
            NotificationLogRequestDTO notificationLogRequestDTO = notificationLogService.notificationLogRequestDTOBuilder(eventId, nabatGroupId, userByEmail.getId(), requestDTO.getMessage());

            notificationLogService.save(notificationLogRequestDTO);
        }
    }

    @Override
    public NotifyResult getNotifyResult(UUID eventId){
        NotificationLog notificationLog = notificationLogRepository.findByEventId(eventId).orElseThrow(() -> new ClientBackendException(ErrorCode.NOTIFICATION_LOG_NOT_FOUND));

        try {
            NotifyResult notifyResult = notifyResultClient.getNotifyResult(eventId);

            // Serialize NotifyResult to JSON
            String notifyResultJson = mapper.writeValueAsString(notifyResult);

            NotificationLogRequestDTO notificationLogRequestDTO = notificationLogService.notificationLogRequestDTOBuilder(notifyResultJson);

            notificationLogService.patch(notificationLog.getId(), notificationLogRequestDTO);

            return notifyResult;
        } catch (Exception e) {
            throw new ClientBackendException(ErrorCode.GET_NOTIFICATION_RESULT_FAILED);
        }
    }
}
