package com.example.testtaskeffectmobile.service.impl;

import com.example.testtaskeffectmobile.dto.RegisterUserDto;
import com.example.testtaskeffectmobile.exception.RoleNotFoundException;
import com.example.testtaskeffectmobile.mapper.UserMapper;
import com.example.testtaskeffectmobile.model.Role;
import com.example.testtaskeffectmobile.model.User;
import com.example.testtaskeffectmobile.repository.RoleRepository;
import com.example.testtaskeffectmobile.repository.UserRepository;
import com.example.testtaskeffectmobile.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    private static final String USER_ROLE_NAME = "USER";

    @Override
    @Transactional
    public void save(RegisterUserDto registerUserDto) {
        log.info("Creating new user entity for: {}", registerUserDto.getEmail());
        User userDB = userMapper.toNewEntity(registerUserDto);

        Role role = roleRepository.findByName(USER_ROLE_NAME)
                .orElseThrow(() -> {
                    log.error("Role with name {} wasn't found", USER_ROLE_NAME);
                    throw new RoleNotFoundException(String.format("Role with name %s wasn't found", USER_ROLE_NAME));
                });

        userDB.setPassword(passwordEncoder.encode(userDB.getPassword()));
        userDB.getRoles().add(role);
        userRepository.save(userDB);
        log.info("User {} registered successfully", userDB.getEmail());
    }

}
