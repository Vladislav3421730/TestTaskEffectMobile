package com.example.testtaskeffectmobile.service.impl;

import com.example.testtaskeffectmobile.dto.responce.JwtResponseDto;
import com.example.testtaskeffectmobile.dto.request.LoginUserRequestDto;
import com.example.testtaskeffectmobile.dto.request.RegisterUserRequestDto;
import com.example.testtaskeffectmobile.exception.LoginFailedException;
import com.example.testtaskeffectmobile.exception.PasswordsNotTheSameException;
import com.example.testtaskeffectmobile.exception.RegistrationFailedException;
import com.example.testtaskeffectmobile.exception.UserNotFoundException;
import com.example.testtaskeffectmobile.model.User;
import com.example.testtaskeffectmobile.repository.UserRepository;
import com.example.testtaskeffectmobile.service.AuthService;
import com.example.testtaskeffectmobile.service.UserService;
import com.example.testtaskeffectmobile.utils.JwtAccessTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtAccessTokenUtils jwtAccessTokenUtils;

    @Override
    public JwtResponseDto createAuthToken(LoginUserRequestDto user) {
        try {
            log.info("Attempting authentication for user: {}", user.getEmail());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        } catch (BadCredentialsException badCredentialsException) {
            log.error("error: {}", badCredentialsException.getMessage());
            throw new LoginFailedException("Invalid username or password");
        }

        User userDB = userRepository.findByEmail(user.getEmail()).orElseThrow(() ->
                new UserNotFoundException(String.format("User with email %s was not found", user.getEmail())));

        log.info("User {} authenticated successfully", user.getEmail());
        return new JwtResponseDto(jwtAccessTokenUtils.generateAccessToken(userDB));
    }

    @Override
    public void registerUser(RegisterUserRequestDto user) {
        log.info("Starting registration process for user: {}", user.getEmail());

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            log.error("Password mismatch for user: {}", user.getEmail());
            throw new PasswordsNotTheSameException("Passwords should be the same");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            log.error("Email {} is already in use", user.getEmail());
            throw new RegistrationFailedException(String.format("User with email %s already exists in the system", user.getEmail()));
        }
        userService.save(user);
    }

}
