package com.reporthub.controller.api.auth;

import com.reporthub.config.AppConfig;
import com.reporthub.dto.UserDTO;
import com.reporthub.dto.auth.LoginRequest;
import com.reporthub.dto.auth.RegisterRequest;
import com.reporthub.entity.User;
import com.reporthub.service.IUserService;
import com.reporthub.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.JDBCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("api/v1/auth/")
public class AuthController {

    @Autowired
    private IUserService userService;
    @Autowired
    private JwtService jwtService;


    private Cookie generateCookie(String JWT, String path) {
        Cookie cookie = new Cookie("JWT", JWT);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath(AppConfig.getAPILink() + path);
        cookie.setMaxAge(60*60*600);
        return cookie;
    }

    @ExceptionHandler({SQLException.class, JDBCException.class})
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request, HttpServletResponse response)  {
        Map<String, String> message = new HashMap<>();

        if(userService.findByEmail(request.getEmail()) != null) {
            message.put("message", "User with this email already exists.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }

        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

            UserDTO loggedUser = new UserDTO(
                userService.save(
                    new User(request.getUsername(), request.getEmail(), encoder.encode(request.getPassword()), request.getPhoneNumber())
                )
            );

            String JWT = userService.verify(loggedUser.getUsername(), request.getPassword());
            loggedUser.attributes.put("JWT", JWT);
            response.addCookie(this.generateCookie(JWT, "/auth/register"));

            return ResponseEntity.ok(loggedUser);

        } catch (Exception e) {
            message.put("message", e.getCause().getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {

        UserDTO user = new UserDTO(userService.findByUsername(request.getUsername()));

        String JWT = userService.verify(request.getUsername(), request.getPassword());
        if(Objects.equals(JWT, "Failed")) {
            Map<String, String> message = new HashMap<>();
            message.put("message", "User credentials are incorrect.");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(message);
        }

        if(user.getBanned()) {
            Map<String, String> message = new HashMap<>();
            message.put("message", "You're account has been banned. You may check your e-mail inbox for more information.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
        }

        user.attributes.put("JWT", JWT);
        response.addCookie(this.generateCookie(JWT, "/auth/login"));

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping("/logout")
    @PreAuthorize("@authorizationService.isConnected(authentication.principal.id)")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("JWT", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);



        Map<String, String> message = new HashMap<>();
        message.put("message", "User has been logged out.");

        return ResponseEntity.status(HttpStatus.OK).body(message);
    }
}