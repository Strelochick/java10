package com.mpt.dentalclinic.service;

import com.mpt.dentalclinic.entity.UserEntity;
import com.mpt.dentalclinic.repository.UserRepository;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    UserEntity user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

    if (!user.isActive()) {
        throw new UsernameNotFoundException("User is not active: " + email);
    }

    return User.builder()
            .username(user.getEmail())
            .password(user.getPassword())
            .authorities(user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                    .collect(Collectors.toList()))
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(!user.isActive())
            .build();
}
}