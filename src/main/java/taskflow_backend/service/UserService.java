package taskflow_backend.service;

import taskflow_backend.dto.LoginRequest;
import taskflow_backend.dto.RegisterRequest;

public interface UserService {

    void register(
            RegisterRequest request
    );

    String login(
            LoginRequest request
    );
}