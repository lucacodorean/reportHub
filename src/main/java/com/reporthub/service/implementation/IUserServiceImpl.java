package com.reporthub.service.implementation;

import com.reporthub.config.AppConfig;
import com.reporthub.dto.TagDTO;
import com.reporthub.dto.UserDTO;
import com.reporthub.entity.Tag;
import com.reporthub.entity.User;
import com.reporthub.entity.auth.Authenticated;
import com.reporthub.repository.IUserRepository;
import com.reporthub.request.api.auth.UserLoginRequest;
import com.reporthub.request.api.auth.UserRegisterRequest;
import com.reporthub.request.api.v1.UserUpdateRequest;
import com.reporthub.service.IMailService;
import com.reporthub.service.IUserService;
import com.reporthub.service.JwtService;
import com.reporthub.service.Response;
import com.reporthub.exception.IncorrectCredentialsException;
import com.reporthub.exception.NotFoundException;
import com.reporthub.exception.UserAlreadyExistsException;
import com.reporthub.exception.UserBannedException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class IUserServiceImpl implements IUserService {

    @Autowired private IUserRepository userRepository;

    @Autowired private JwtService jwtService;

    @Autowired private IMailService mailService;

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

    private User findByEmail(String email) {return userRepository.findByEmail(email).orElse(null); }

    private User findByUsername(String username ) { return userRepository.findByUsername(username); }

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

    private Cookie generateCookie(String JWT, String path) {
        Cookie cookie = new Cookie("JWT", JWT);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath(AppConfig.getAPILink() + path);
        cookie.setMaxAge(60*60*600);
        return cookie;
    }

    @Override
    public Response<UserDTO> create(UserRegisterRequest request,  HttpServletResponse httpResponse) {
        Map<String, String> message = new HashMap<>();

        try {
            if(this.findByEmail(request.getEmail()) != null)
                throw new UserAlreadyExistsException("User with this email already exists.");

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

            UserDTO loggedUser = new UserDTO(
                this.save(
                    new User(request.getUsername(), request.getEmail(), encoder.encode(request.getPassword()), request.getPhoneNumber())
                )
            );

            String JWT = this.verify(loggedUser.getUsername(), request.getPassword());
            loggedUser.attributes.put("JWT", JWT);
            httpResponse.addCookie(this.generateCookie(JWT, "/auth/register"));
            return new Response<>(loggedUser, message);
        }
        catch (UserAlreadyExistsException ex) {message.put("message", ex.getMessage());}

        return new Response<>(null, message);
    }

    @Override
    public Response<UserDTO> login(UserLoginRequest request, HttpServletResponse httpResponse) {
        Map<String, String> message = new HashMap<>();
        try {
            UserDTO user = new UserDTO(this.findByUsername(request.getUsername()));

            String JWT = this.verify(request.getUsername(), request.getPassword());
            if (Objects.equals(JWT, "Failed"))
                throw new IncorrectCredentialsException("User credentials are incorrect.");

            if (user.getBanned())
                throw new UserBannedException("You're account has been banned. You may check your e-mail inbox for more information.");

            user.attributes.put("JWT", JWT);
            httpResponse.addCookie(this.generateCookie(JWT, "/auth/login"));
            return new Response<>(user, null);
        }
        catch (IncorrectCredentialsException | UserBannedException  ex) { message.put("message", ex.getMessage()); }
        return new Response<>(null, message);
    }

    @Override
    public boolean logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("JWT", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return true;
    }

    @Override
    public Response<UserDTO> update(String key, UserUpdateRequest request) {
        Map<String, String> message = new HashMap<>();

        try {
            User user = this.findByKey(key);
            if (user == null) throw new NotFoundException("User not found");

            if (request.getUsername() != null) user.setUsername(request.getUsername());
            if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
            if (request.getEmail() != null) user.setEmail(request.getEmail());
            if (request.getScore() != null) user.setScore(request.getScore());
            if (request.getBanned() != null && user.getIsBanned() != request.getBanned()) {
                String stringBuilder = "Dear " + user.getUsername() +
                        ", \nWe're contacting you in regards of your reportHub account.\n\n" +
                        "Your access on the platform has been " +
                        (request.getBanned() ?
                                "revoked due to the failure to respect community's policy. \nYou may need to contact community's administrators in order to re-gain access to your account.\n"
                                : "authorized. You may log-in again on reportHub.\n") +
                        "\nKind regards,\nteam @reportHub";

                mailService.sendMail(user.getEmail(), "reportHub - about your account", stringBuilder);
                user.setIsBanned(request.getBanned());
            }
            if (request.getPassword() != null) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
                user.setPassword(encoder.encode(request.getPassword()));
            }

            return new Response<>(new UserDTO(this.save(user)), null);
        }  catch (NotFoundException ex) { message.put("message", ex.getMessage()); }

        return new Response<>(null, message);
    }

    @Override
    public List<UserDTO> all() {
        return this.findAll().stream().map(UserDTO::new).collect(Collectors.toList());
    }

    @Override
    public Response<UserDTO> retrieveDTO(String key)  {
        Map<String, String> message = new HashMap<>();
        try {
            User user = this.findByKey(key);
            if (user == null) throw new NotFoundException("User not found");
            return new Response<>(new UserDTO(user), null);
        } catch (NotFoundException e) { message.put("message", "User not found"); }
        return new Response<>(null, message);
    }
}
