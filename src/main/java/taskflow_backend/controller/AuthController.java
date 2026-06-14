package taskflow_backend.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import taskflow_backend.dto.ApiResponse;
import taskflow_backend.dto.AuthResponse;
import taskflow_backend.dto.LoginRequest;
import taskflow_backend.dto.RegisterRequest;
import taskflow_backend.service.UserService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Object>>
    register(
            @RequestBody RegisterRequest request){

        userService.register(request);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message(
                                "User registered successfully"
                        )
                        .data(null)
                        .timestamp(
                                LocalDateTime.now()
                        )
                        .build()
        );
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>>
    login(
            @RequestBody LoginRequest request){

        String token =
                userService.login(request);

        AuthResponse response =
                AuthResponse.builder()
                        .token(token)
                        .build();

        return ResponseEntity.ok(
                ApiResponse.<AuthResponse>builder()
                        .success(true)
                        .message("Login successful")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}