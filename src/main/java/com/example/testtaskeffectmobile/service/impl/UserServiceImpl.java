package com.example.testtaskeffectmobile.service.impl;

import com.example.testtaskeffectmobile.dto.request.BannedRequestDto;
import com.example.testtaskeffectmobile.dto.request.RegisterUserRequestDto;
import com.example.testtaskeffectmobile.exception.RoleNotFoundException;
import com.example.testtaskeffectmobile.exception.UserNotFoundException;
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

import java.util.UUID;

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
    public void save(RegisterUserRequestDto registerUserRequestDto) {
        log.info("Creating new user entity for: {}", registerUserRequestDto.getEmail());
        User userDB = userMapper.toNewEntity(registerUserRequestDto);

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

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!userRepository.existsById(id)) {
            log.error("User with id {} wasn't found", id);
            throw new UserNotFoundException(String.format("User with id %s wasn't found", id));
        }
        userRepository.deleteById(id);
        log.info("User with id {} was successfully deleted", id);
    }

    @Override
    @Transactional
    public void banUser(UUID id, BannedRequestDto bannedRequestDto) {
        User user = findById(id);
        userRepository.save(user);
        log.info("User was successfully updated, set ban status: {}", bannedRequestDto.getBanned());
    }

    @Override
    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User with id {} wasn't found", id);
                    throw new UserNotFoundException(String.format("User with id %s wasn't found", id));
                });
    }


}
