package com.reporthub.service.implementation;

import com.reporthub.entity.User;
import com.reporthub.entity.auth.Authenticated;
import com.reporthub.repository.IUserRepository;
import com.reporthub.service.IUserService;
import com.reporthub.service.JwtService;
import exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IUserServiceImpl implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    public User save(User entity) { return userRepository.save(entity); }

    public User findById(Long id) throws NotFoundException {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) return user.get();
        else throw new NotFoundException("User not found");
    }

    public User findByKey(String key) throws NotFoundException  {
        Optional<User> user = userRepository.findByKey(key);
        if(user.isPresent()) return user.get();
        else throw new NotFoundException("User not found");
    }

    public User findByEmail(String email) {return userRepository.findByEmail(email).orElse(null); }

    public User findByUsername(String username ) { return userRepository.findByUsername(username); }

    public List<User> findAll() { return userRepository.findAll(); }

    public boolean delete(User entity) throws NotFoundException  {
        if(!userRepository.existsById(entity.getId())) throw new NotFoundException("User not found");

        userRepository.delete(entity);
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);
        if (user == null) {
            System.out.printf("User not found: %s\n", username);
            throw new UsernameNotFoundException(username);
        }
        return new Authenticated(user);
    }

    @Override
    public String verify(String username, String password) {

        User temp = userRepository.findByUsername(username);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        if(temp == null || !encoder.matches(password, temp.getPassword())) return "Failed";

        return jwtService.generateToken(username);
    }
}
