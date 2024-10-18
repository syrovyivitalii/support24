package lv.dsns.support24.user.service.impl;

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
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserServiceImpl implements UserService {
    private final SystemUsersRepository systemUsersRepository;
    private final UserMapper userMapper;
    private final UnitService unitService;

    @Value("${userDefaultPassword}")
    private String DEFAULT_PASSWORD;

    @Autowired
    public UserServiceImpl(SystemUsersRepository systemUsersRepository, UserMapper userMapper, UnitService unitService) {
        this.systemUsersRepository = systemUsersRepository;
        this.userMapper = userMapper;
        this.unitService = unitService;
    }

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

    public PageResponse<UserResponseDTO> findAllSubordinatedPageable(Principal principal, UserFilter userFilter, Pageable pageable){
        var authUser = systemUsersRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ClientBackendException(ErrorCode.USER_NOT_FOUND));

        List<UnitResponseDTO> allChildUnits = unitService.findAllChildUnits(authUser.getUnitId());

        Set<UUID> childUnitsIds = allChildUnits.stream()
                .map(UnitResponseDTO::getId)
                .collect(Collectors.toSet());

        // If user has added a specific unit filter, ensure it belongs to the child units
        if (userFilter.getUnits() != null && !userFilter.getUnits().isEmpty()) {
            // Check if any unit in userFilter is not part of child units
            boolean hasInvalidUnits = userFilter.getUnits().stream()
                    .anyMatch(unitId -> !childUnitsIds.contains(unitId));

            // If invalid units are found, throw an exception or handle as required
            if (hasInvalidUnits) {
                throw new ClientBackendException(ErrorCode.INVALID_UNIT);
            }

            // Filter the user's units to only include those in the child units
            userFilter.getUnits().retainAll(childUnitsIds);
        } else {
            // If no specific units were in the filter, apply all child units
            userFilter.setUnits(childUnitsIds);
        }

        return findAllPageable(userFilter,pageable);
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

    private Specification<SystemUsers> getSearchSpecification(UserFilter userFilter) {
        return Specification.where((Specification<SystemUsers>) searchLikeString("email", userFilter.getEmail()))
                .and((Specification<SystemUsers>) searchOnRole(userFilter.getRoles()))
                .and((Specification<SystemUsers>) searchOnUserStatus(userFilter.getStatuses()))
                .and((Specification<SystemUsers>) searchOnShifts(userFilter.getShifts()))
                .and((Specification<SystemUsers>) searchLikeString("name", userFilter.getName()))
                .and((Specification<SystemUsers>) searchFieldInCollectionOfIds("unitId", userFilter.getUnits()))
                .and((Specification<SystemUsers>) searchFieldInCollectionOfIds("positionId", userFilter.getPositions()))
                .and((Specification<SystemUsers>) searchFieldInCollectionOfIds("rankId", userFilter.getRanks()));
    }
}
