package com.reporthub.service.implementation;

import com.reporthub.entity.Postable;
import com.reporthub.entity.Report;
import com.reporthub.entity.User;
import com.reporthub.entity.auth.Authenticated;
import com.reporthub.repository.IPostRepository;
import com.reporthub.repository.IUserRepository;
import com.reporthub.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IPostServiceImpl {

    @Autowired
    private IPostRepository postRepository;

    @Autowired
    private JwtService jwtService;

    public Postable save(Postable entity) {
        return postRepository.save(entity);
    }

    public Postable findById(Long id) { return postRepository.findById(id).orElse(null); }

    public Postable findByKey(String key) { return postRepository.findByKey(key).orElse(null); }

    public List<Postable> findAll() { return postRepository.findAll(); }

    public List<Report> findAllReports{ return postRepository.}

    public boolean delete(Postable entity) {
        if(!postRepository.existsById(entity.getId())) return false;

        postRepository.delete(entity);
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = postRepository.findByUsername(username);
        if (user == null) {
            System.out.printf("User not found: %s\n", username);
            throw new UsernameNotFoundException(username);
        }
        return new Authenticated(user);
    }

    @Override
    public String verify(String username, String password) {

        User temp = postRepository.findByUsername(username);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        if(temp == null || !encoder.matches(password, temp.getPassword())) return "Failed";

        return jwtService.generateToken(username);
    }

    @Override
    public User retrieveLoggedUser(String authHeader) {
        String tokenString = authHeader.replace("Bearer ", "");
        String username = jwtService.extractUsername(tokenString);

        return postRepository.findByUsername(username);
    }
}
