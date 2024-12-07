package lv.dsns.support24.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.dsns.support24.common.dto.response.PageResponse;
import lv.dsns.support24.common.exception.ClientBackendException;
import lv.dsns.support24.common.exception.ErrorCode;
import lv.dsns.support24.unit.controller.dto.response.UnitResponseDTO;
import lv.dsns.support24.unit.service.UnitService;
import lv.dsns.support24.user.controller.dto.enums.UserStatus;
import lv.dsns.support24.user.controller.dto.request.UserDefaultRequestDTO;
import lv.dsns.support24.user.controller.dto.request.UserRequestDTO;
import lv.dsns.support24.user.controller.dto.response.UserResponseDTO;
import lv.dsns.support24.user.mapper.UserMapper;
import lv.dsns.support24.user.repository.SystemUsersRepository;
import lv.dsns.support24.user.repository.entity.SystemUsers;
import lv.dsns.support24.user.service.UserService;
import lv.dsns.support24.user.service.filter.UserFilter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import static lv.dsns.support24.common.specification.SpecificationCustom.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final SystemUsersRepository systemUsersRepository;
    private final UserMapper userMapper;
    private final UnitService unitService;

    @Value("${userDefaultPassword}")
    private String DEFAULT_PASSWORD;

    @Override
    public List<UserResponseDTO> findAll(UserFilter userFilter){
        var allSystemUsers = systemUsersRepository.findAll(getSearchSpecification(userFilter), Sort.by(Sort.Direction.ASC, "name"));

        return allSystemUsers.stream().map(userMapper::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public PageResponse<UserResponseDTO> findAllPageable(UserFilter userFilter, Pageable pageable){
        var allSystemUsers = systemUsersRepository.findAll(getSearchSpecification(userFilter), pageable);

        List<UserResponseDTO> userDTOs = allSystemUsers.stream()
                .map(userMapper::mapToDTO)
                .collect(Collectors.toList());

        return PageResponse.<UserResponseDTO>builder()
                .totalPages((long) allSystemUsers.getTotalPages())
                .pageSize((long) pageable.getPageSize())
                .totalElements(allSystemUsers.getTotalElements())
                .content(userDTOs)
                .build();
    }

    @Override
    public PageResponse<UserResponseDTO> findAllSubordinatedPageable(Principal principal, UserFilter userFilter, Pageable pageable) {
        var authUser = systemUsersRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ClientBackendException(ErrorCode.USER_NOT_FOUND));

        List<UnitResponseDTO> allChildUnits = unitService.findAllChildUnits(authUser.getPermissionUnitId());
        Set<UUID> childUnitsIds = allChildUnits.stream()
                .map(UnitResponseDTO::getId)
                .collect(Collectors.toSet());

        validateAndSetUserFilterUnits(userFilter, childUnitsIds);

        return findAllPageable(userFilter, pageable);
    }

    @Override
    public List<UserResponseDTO> findAllSubordinated(Principal principal, UserFilter userFilter) {
        var authUser = systemUsersRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ClientBackendException(ErrorCode.USER_NOT_FOUND));

        List<UnitResponseDTO> allChildUnits = unitService.findAllChildUnits(authUser.getPermissionUnitId());
        Set<UUID> childUnitsIds = allChildUnits.stream()
                .map(UnitResponseDTO::getId)
                .collect(Collectors.toSet());

        validateAndSetUserFilterUnits(userFilter, childUnitsIds);

        return findAll(userFilter);
    }

    private void validateAndSetUserFilterUnits(UserFilter userFilter, Set<UUID> childUnitsIds) {
        if (userFilter.getUnits() != null && !userFilter.getUnits().isEmpty()) {
            boolean hasInvalidUnits = userFilter.getUnits().stream()
                    .anyMatch(unitId -> !childUnitsIds.contains(unitId));

            if (hasInvalidUnits) {
                throw new ClientBackendException(ErrorCode.INVALID_UNIT);
            }

            userFilter.getUnits().retainAll(childUnitsIds);
        } else {
            userFilter.setUnits(childUnitsIds);
        }
    }


    @Override
    @Transactional
    public UserResponseDTO save(UserRequestDTO userRequestDTO) {
        if ((systemUsersRepository.existsByEmail(userRequestDTO.getEmail()))) {
            throw new ClientBackendException(ErrorCode.USER_ALREADY_EXISTS);
        }

        var user = userMapper.mapToEntity(userRequestDTO);
        var savedUser = systemUsersRepository.save(user);

        return userMapper.mapToDTO(savedUser);
    }

    @Override
    public UserResponseDTO saveDefault(UserDefaultRequestDTO userDefaultRequestDTO) {
        if (systemUsersRepository.existsByEmail(userDefaultRequestDTO.getEmail())) {
            throw new ClientBackendException(ErrorCode.USER_ALREADY_EXISTS);
        }

        var user = userMapper.mapToDefaultEntity(userDefaultRequestDTO, DEFAULT_PASSWORD);
        var savedUser = systemUsersRepository.save(user);

        return userMapper.mapToDTO(savedUser);
    }
    @Override
    @Transactional
    public UserResponseDTO patch (UUID id, UserRequestDTO requestDTO){
        var userById = systemUsersRepository.findById(id)
                .orElseThrow(() -> new ClientBackendException(ErrorCode.USER_NOT_FOUND));

        userMapper.patchMerge(requestDTO,userById);

        return userMapper.mapToDTO(userById);
    }

    @Override
    public void delete(UUID id){
        var problemById = systemUsersRepository.findById(id)
                .orElseThrow(() -> new ClientBackendException(ErrorCode.USER_NOT_FOUND));

        problemById.setStatus(UserStatus.DELETED);

        systemUsersRepository.save(problemById);
    }

    @Override
    public boolean existUserByEmail(String email){
        return systemUsersRepository.existsByEmail(email);
    }

    @Override
    public UserResponseDTO getUserByEmail(String email){
        var byEmail = systemUsersRepository.findByEmail(email).orElseThrow(() -> new ClientBackendException(ErrorCode.USER_NOT_FOUND));

        return userMapper.mapToDTO(byEmail);
    }

    private Specification<SystemUsers> getSearchSpecification(UserFilter userFilter) {
        return Specification.where((Specification<SystemUsers>) searchLikeString("email", userFilter.getEmail()))
                .and((Specification<SystemUsers>) searchOnField("role", userFilter.getRoles()))
                .and((Specification<SystemUsers>) searchOnField("status", userFilter.getStatuses()))
                .and((Specification<SystemUsers>) searchOnField("shift", userFilter.getShifts()))
                .and((Specification<SystemUsers>) searchLikeString("name", userFilter.getName()))
                .and((Specification<SystemUsers>) searchFieldInCollectionOfIds("unitId", userFilter.getUnits()))
                .and((Specification<SystemUsers>) searchFieldInCollectionOfIds("positionId", userFilter.getPositions()))
                .and((Specification<SystemUsers>) searchFieldInCollectionOfIds("rankId", userFilter.getRanks()))
                .and((Specification<SystemUsers>) searchFieldInCollectionOfIntegerIds("soduId", userFilter.getSoduId()));
    }

}
