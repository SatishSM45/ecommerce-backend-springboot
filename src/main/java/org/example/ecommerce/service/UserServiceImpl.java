package org.example.ecommerce.service;

import lombok.extern.slf4j.Slf4j;
import org.example.ecommerce.common.BaseResponse;
import org.example.ecommerce.common.Result;
import org.example.ecommerce.config.JwtRequestContext;
import org.example.ecommerce.dto.AdminUserRequest;
import org.example.ecommerce.dto.UserRequest;
import org.example.ecommerce.dto.UserResponse;
import org.example.ecommerce.enums.Role_Type;
import org.example.ecommerce.entity.Roles;
import org.example.ecommerce.entity.User;
import org.example.ecommerce.exception.ValidationException;
import org.example.ecommerce.mapper.UserMapper;
import org.example.ecommerce.repository.RolesRepository;
import org.example.ecommerce.repository.UserRepository;
import org.example.ecommerce.util.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtRequestContext jwtRequestContext;

    @Autowired
    private CommonService commonService;

    @Override
    public BaseResponse<UserResponse> registerUser(UserRequest request) {
        // 2. Create User
        Optional<User> existUser = userRepository.findByEmail(request.getEmail());
        if (existUser.isPresent()) {
            throw new ValidationException(1005, "user already found..!", "user already found..!");
        }
        User user = new User();
        user.setMobileNo(request.getMobileNo());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); //
        user.setGender(request.getGender());
        user.setStatus(true);
        user.setDateOfBirth(request.getDateOfBirth());

        User savedUser = userRepository.save(user);

        // 3. Assign Role
        Roles role = new Roles();
        role.setRole(Role_Type.USER);
        role.setUser(savedUser);
        role.setStatus(true);
        rolesRepository.save(role);

        // 4. Map Response
        UserResponse userResponse = userMapper.entityToUser(savedUser);

        Result result = new Result();
        result.setSuccessCode(0);
        result.setSuccessDescription("Success");

        BaseResponse<UserResponse> baseResponse = new BaseResponse<>();
        baseResponse.setData(userResponse);
        baseResponse.setResult(result);

        return baseResponse;
    }

    @Override
    public BaseResponse<UserResponse> createAdminUser(AdminUserRequest request) {
        log.info("AdminUserRequest : {} ", request);
        Optional<User> existUser = userRepository.findByEmail(request.getEmail());
        if (existUser.isPresent()) {
            throw new ValidationException(1005, "user already found..!", "user already found..!");
        }
        User user = new User();
        user.setMobileNo(request.getMobileNo());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); //
        user.setGender(request.getGender());
        user.setStatus(true);
        user.setDateOfBirth(request.getDateOfBirth());
        User savedUser = userRepository.save(user);
        // 3. Assign Role
        Roles role = new Roles();
        role.setRole(Role_Type.valueOf(request.getRole()));
        role.setUser(savedUser);
        role.setStatus(true);
        rolesRepository.save(role);
        // 4. Map Response
        UserResponse userResponse = userMapper.entityToUser(savedUser);
        Result result = new Result();
        result.setSuccessCode(0);
        result.setSuccessDescription("Success");
        BaseResponse<UserResponse> baseResponse = new BaseResponse<>();
        baseResponse.setData(userResponse);
        baseResponse.setResult(result);
        return baseResponse;
    }

    @Override
    public BaseResponse<UserResponse> updateUser(UserRequest request) {
        String email = jwtRequestContext.getEmail();
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new ValidationException(1005, "user not found..!", "user not found..!"));
        if (!email.equalsIgnoreCase(user.getEmail())) {
            throw new ValidationException(1018, "user email mismatch..!", "user email mismatch..!");
        }
        if (request.getName() != null) {
            user.setName(request.getName());
        }

        if (request.getMobileNo() != null) {
            user.setMobileNo(request.getMobileNo());
        }

        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }

        if (request.getDateOfBirth() != null) {
            user.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        User savedUser = userRepository.save(user);
        UserResponse userResponse = userMapper.entityToUser(savedUser);
        Result result = new Result();
        result.setSuccessCode(0);
        result.setSuccessDescription("Success");

        BaseResponse<UserResponse> baseResponse = new BaseResponse<>();
        baseResponse.setData(userResponse);
        baseResponse.setResult(result);

        return baseResponse;
    }

    @Override
    public BaseResponse softDeleteUser(String email) {
        log.info("delete User emailId : {} ", email);
        //fetching by email
        String emailJwt = jwtRequestContext.getEmail();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ValidationException(1005, "user not found..!", "user not found..!"));
        if (!email.equalsIgnoreCase(emailJwt)) {
            throw new ValidationException(1018, "user email mismatch..!", "user email mismatch..!");
        }
        //editing or making deactive user
        user.setStatus(false);
        //save existing user object
        userRepository.save(user);
        //setting response
        Result result = new Result();
        result.setSuccessCode(0);
        result.setSuccessDescription("Success");
        BaseResponse<UserResponse> baseResponse = new BaseResponse<>();
        baseResponse.setData(null);
        baseResponse.setResult(result);
        return baseResponse;
    }
}
