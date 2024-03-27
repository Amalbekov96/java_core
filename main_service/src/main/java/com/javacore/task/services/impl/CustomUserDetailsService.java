package com.javacore.task.services.impl;


import com.javacore.task.entities.User;
import com.javacore.task.exceptions.BadRequestException;
import com.javacore.task.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getEntityByUsername(username);
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(getAuthorities())
                .build();
    }

    public User getEntityByUsername(String username) {
        Optional<User> optionalUser = userRepository.findUserByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new BadRequestException("There is no user with username: " + username);
        }

        return optionalUser.get();
    }

    private static Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("USER");
    }
}
