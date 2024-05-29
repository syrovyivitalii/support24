package lv.dsns.support24.user.service.impl;

import lombok.extern.slf4j.Slf4j;
import lv.dsns.support24.user.controller.dto.request.UserRequestDTO;
import lv.dsns.support24.user.controller.dto.response.UserResponseDTO;
import lv.dsns.support24.user.mapper.UserMapper;
import lv.dsns.support24.user.repository.SystemUsersRepository;
import lv.dsns.support24.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
        var allSystemUsers = systemUsersRepository.findAll();
        return allSystemUsers.stream().map(userMapper::mapToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponseDTO save(UserRequestDTO userRequestDTO) {
        var user = userMapper.mapToEntity(userRequestDTO);
        var savedUser = systemUsersRepository.save(user);

        return userMapper.mapToDTO(savedUser);
    }

}
