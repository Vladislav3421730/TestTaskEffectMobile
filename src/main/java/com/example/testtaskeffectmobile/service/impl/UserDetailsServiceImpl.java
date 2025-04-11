package com.example.testtaskeffectmobile.service.impl;

import com.example.testtaskeffectmobile.model.User;
import com.example.testtaskeffectmobile.repository.UserRepository;
import com.example.testtaskeffectmobile.wrapper.UserDetailsWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email %s not found ", email)));
        UserDetailsWrapper userDetailsWrapper = new UserDetailsWrapper(user);
        if (!userDetailsWrapper.isEnabled()) {
            throw new DisabledException("You were banned");
        }
        return new UserDetailsWrapper(user);
    }

}
