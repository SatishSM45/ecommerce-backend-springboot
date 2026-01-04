package org.example.ecommerce.service;

import org.example.ecommerce.common.BaseResponse;
import org.example.ecommerce.dto.LoginRequest;
import org.example.ecommerce.dto.LoginResponse;

public interface AuthService {
     BaseResponse<LoginResponse> login(LoginRequest request);
}
