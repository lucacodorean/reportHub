package com.reporthub.service;

import com.reporthub.dto.UserDTO;

import com.reporthub.entity.User;
import com.reporthub.request.api.auth.UserLoginRequest;
import com.reporthub.request.api.auth.UserRegisterRequest;
import com.reporthub.request.api.v1.UserUpdateRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface IUserService extends IEntityService<User>, UserDetailsService {
    String verify(String username, String password);

    Response<UserDTO> login(UserLoginRequest userLoginRequest, HttpServletResponse response);
    Response<UserDTO> create(UserRegisterRequest userRegisterRequest, HttpServletResponse response);
    Response<UserDTO> update(String key, UserUpdateRequest userUpdateRequest);

    List<UserDTO> all();
    Response<UserDTO> retrieveDTO(String key);

    boolean logout(HttpServletResponse response);
}
