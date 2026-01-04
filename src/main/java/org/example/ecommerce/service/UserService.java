package org.example.ecommerce.service;

import org.example.ecommerce.common.BaseResponse;
import org.example.ecommerce.dto.AdminUserRequest;
import org.example.ecommerce.dto.UserRequest;
import org.example.ecommerce.dto.UserResponse;

public interface UserService {
    BaseResponse<UserResponse> registerUser(UserRequest request);
    BaseResponse<UserResponse> createAdminUser( AdminUserRequest request);
    BaseResponse<UserResponse> updateUser(UserRequest request);
    BaseResponse softDeleteUser(String email);
}
