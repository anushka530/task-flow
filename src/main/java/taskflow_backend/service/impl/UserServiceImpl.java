package taskflow_backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import taskflow_backend.dto.LoginRequest;
import taskflow_backend.dto.RegisterRequest;
import taskflow_backend.entity.User;
import taskflow_backend.enums.Role;
import taskflow_backend.exception.UserAlreadyExistsException;
import taskflow_backend.repository.UserRepository;
import taskflow_backend.security.CustomUserDetails;
import taskflow_backend.security.JwtService;
import taskflow_backend.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl
        implements UserService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder
            passwordEncoder;

    @Override
    public void register(
            RegisterRequest request) {

        if(userRepository.existsByEmail(
                request.getEmail())){

            throw new UserAlreadyExistsException(
                    "Email already exists"
            );
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(
                        passwordEncoder.encode(
                                request.getPassword()
                        )
                )
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }

    @Override
    public String login(
            LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

        CustomUserDetails user =
                (CustomUserDetails)
                        authentication.getPrincipal();

        return jwtService.generateToken(
                user.getUsername(),

                user.getAuthorities()
                        .iterator()
                        .next()
                        .getAuthority()
        );
    }
}