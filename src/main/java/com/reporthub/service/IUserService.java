package com.reporthub.service;

import com.reporthub.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends IEntityService<User>, UserDetailsService {
    User findByEmail(String email);
    String verify(String username, String password);
    User retrieveLoggedUser(String authHeader);
}
