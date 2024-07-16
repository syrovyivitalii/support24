package lv.dsns.support24.user.service.impl;

import lombok.extern.slf4j.Slf4j;
import lv.dsns.support24.common.dto.response.PageResponse;
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

import java.util.List;
import java.util.stream.Collectors;

import static lv.dsns.support24.common.specification.SpecificationCustom.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final SystemUsersRepository systemUsersRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(SystemUsersRepository systemUsersRepository, UserMapper userMapper) {
        this.systemUsersRepository = systemUsersRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserResponseDTO> findAll(){
        var allSystemUsers = systemUsersRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
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
    @Transactional
    public UserResponseDTO save(UserRequestDTO userRequestDTO) {
        var user = userMapper.mapToEntity(userRequestDTO);
        var savedUser = systemUsersRepository.save(user);

        return userMapper.mapToDTO(savedUser);
    }
    @Override
    public boolean existUserByEmail(String email){
        return systemUsersRepository.existsByEmail(email);
    }

    private Specification<SystemUsers> getSearchSpecification(UserFilter userFilter) {
        return Specification.where((Specification<SystemUsers>) searchLikeString("email", userFilter.getEmail()))
                .and((Specification<SystemUsers>) searchLikeString("role", userFilter.getRole()))
                .and((Specification<SystemUsers>) searchLikeString("name", userFilter.getName()));
    }


}
