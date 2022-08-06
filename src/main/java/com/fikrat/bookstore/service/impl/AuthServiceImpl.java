package com.fikrat.bookstore.service.impl;

import com.fikrat.bookstore.constants.Messages;
import com.fikrat.bookstore.dto.auth.JwtResponse;
import com.fikrat.bookstore.dto.auth.LoginRequest;
import com.fikrat.bookstore.dto.auth.RegisterRequest;
import com.fikrat.bookstore.exception.DuplicateUsernameException;
import com.fikrat.bookstore.exception.RoleNotFoundException;
import com.fikrat.bookstore.model.Role;
import com.fikrat.bookstore.model.User;
import com.fikrat.bookstore.model.enums.ERole;
import com.fikrat.bookstore.repository.RoleRepository;
import com.fikrat.bookstore.repository.UserRepository;
import com.fikrat.bookstore.security.jwt.JwtUtils;
import com.fikrat.bookstore.security.service.UserDetailsImpl;
import com.fikrat.bookstore.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public JwtResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        JwtResponse response = new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles);

        return response;
    }

    @Override
    public User registerAsPublisher(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())){
            throw new DuplicateUsernameException(Messages.DUPLICATE_USERNAME);
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        Role rolePublisher = roleRepository.findByName(ERole.ROLE_PUBLISHER)
                .orElse(Role.builder().name(ERole.ROLE_PUBLISHER).build());

        Set<Role> roles = new HashSet<>();
        roles.add(rolePublisher);
        roleRepository.saveAll(roles);
        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Override
    public User registerAsUser(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())){
            throw new DuplicateUsernameException(Messages.DUPLICATE_USERNAME);
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        Role roleUser = roleRepository.findByName(ERole.ROLE_USER)
                .orElse(Role.builder().name(ERole.ROLE_USER).build());

        Set<Role> roles = new HashSet<>();
        roles.add(roleUser);
        roleRepository.saveAll(roles);
        user.setRoles(roles);
        return userRepository.save(user);
    }
}
