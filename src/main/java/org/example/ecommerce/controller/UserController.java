package org.example.ecommerce.controller;

import jakarta.validation.Valid;
import org.example.ecommerce.common.BaseResponse;
import org.example.ecommerce.dto.AdminUserRequest;
import org.example.ecommerce.dto.UserRequest;
import org.example.ecommerce.dto.UserResponse;
import org.example.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<UserResponse>> registerUser(@RequestBody @Valid UserRequest request) {
        return ResponseEntity.ok(userService.registerUser(request));
    }

    @PostMapping("/admin/register")
    public ResponseEntity<BaseResponse<UserResponse>> createAdminUser(@RequestBody AdminUserRequest request) {
        return ResponseEntity.ok(userService.createAdminUser(request));
    }

    @PutMapping("/users")
    public ResponseEntity<BaseResponse<UserResponse>> updateUser(@RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.updateUser(request));
    }

    @DeleteMapping("/admin/users/{email}")
    public ResponseEntity<BaseResponse> deleteUser(@PathVariable("email") String email) {
        return ResponseEntity.ok(userService.softDeleteUser(email));
    }
}
