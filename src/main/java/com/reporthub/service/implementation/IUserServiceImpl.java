package com.reporthub.service.implementation;

import com.reporthub.entity.User;
import com.reporthub.repository.IUserRepository;
import com.reporthub.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IUserServiceImpl implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    public User save(User entity) { return userRepository.save(entity); }

    public User findById(Long id) { return userRepository.findById(id).orElse(null); }

    public User findByKey(String key) { return userRepository.findByKey(key).orElse(null); }

    public List<User> findAll() { return userRepository.findAll(); }

    public boolean delete(User entity) {
        if(!userRepository.existsById(entity.getId())) return false;

        userRepository.delete(entity);
        return true;
    }

}
