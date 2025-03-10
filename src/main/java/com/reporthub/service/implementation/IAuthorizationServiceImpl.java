package com.reporthub.service.implementation;

import com.reporthub.entity.User;
import com.reporthub.repository.IUserRepository;
import com.reporthub.service.IAuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("authorizationService")
public class IAuthorizationServiceImpl implements IAuthorizationService {

    @Autowired private IUserRepository userRepository;

    @Override
    public boolean isAdmin(Long authenticatedId) {
        Optional<User> user = userRepository.findById(authenticatedId);
        return user.isPresent() && user.get().getIsModerator();
    }

    @Override
    public boolean canEditUser(Long authenticatedId, String target) {
        Optional<User> user = userRepository.findByKey(target);

        if (user.isPresent() && user.get().getIsBanned()) return isAdmin(authenticatedId);
        return (user.isPresent() && user.get().getId().equals(authenticatedId)) || isAdmin(authenticatedId);
    }

    @Override
    public boolean canDeleteUser(Long authenticatedId, String target) {
        Optional<User> user = userRepository.findByKey(target);
        return (user.isPresent() && user.get().getId().equals(authenticatedId)) || isAdmin(authenticatedId);
    }
}
