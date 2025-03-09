package com.reporthub.controller.api.auth;

import com.reporthub.dto.UserDTO;
import com.reporthub.dto.auth.LoginRequest;
import com.reporthub.dto.auth.RegisterRequest;
import com.reporthub.entity.User;
import com.reporthub.service.IUserService;
import com.reporthub.service.JwtService;
import com.reporthub.singleton.ServiceSingleton;
import org.hibernate.JDBCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("api/v1/auth/")
public class AuthController {

    @Autowired
    private final IUserService userService = ServiceSingleton.getUserService();
    @Autowired
    private JwtService jwtService;

    @ExceptionHandler({SQLException.class, JDBCException.class})
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request)  {
        if(userService.findByEmail(request.getEmail()) != null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        try {
            UserDTO loggedUser = new UserDTO(
                userService.save(
                    new User(request.getUsername(), request.getEmail(), request.getPassword(), request.getPhoneNumber())
                )
            );

            loggedUser.attributes.put("JWT", userService.verify(loggedUser.getUsername(), request.getPassword()));
            return ResponseEntity.ok(loggedUser);

        } catch (DataIntegrityViolationException e) {
            Map<String, String> message = new HashMap<>();
            message.put("message", e.getCause().getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        UserDTO user = new UserDTO(userService.findByUsername(request.getUsername()));
        String JWT = userService.verify(request.getUsername(), request.getPassword());
        if(Objects.equals(JWT, "Failed")) {
            Map<String, String> message = new HashMap<>();
            message.put("message", "User credentials are incorrect.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }

        user.attributes.put("JWT", JWT);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/connected")
    public ResponseEntity<UserDTO> getLoggedInUser(@RequestHeader("Authorization") String authHeader) {
        User user = userService.retrieveLoggedUser(authHeader);
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(new UserDTO(user));
    }
}