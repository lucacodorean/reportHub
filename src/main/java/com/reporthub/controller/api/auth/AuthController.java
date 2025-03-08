package com.reporthub.controller.api.auth;

import com.reporthub.dto.UserDTO;
import com.reporthub.dto.auth.LoginRequest;
import com.reporthub.dto.auth.RegisterRequest;
import com.reporthub.entity.User;
import com.reporthub.service.IUserService;
import com.reporthub.service.JwtService;
import com.reporthub.singleton.ServiceSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth/")
public class AuthController {

    @Autowired
    private final IUserService userService = ServiceSingleton.getUserService();
    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if(userService.findByEmail(request.getEmail()) != null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        UserDTO loggedUser = new UserDTO(
            userService.save(
                new User(request.getUsername(), request.getEmail(), request.getPassword(), request.getPhoneNumber())
            )
        );

        return ResponseEntity.ok(userService.verify(loggedUser.getUsername(), request.getPassword()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(
            userService.verify(request.getUsername(), request.getPassword())
        );
    }

    @GetMapping("/connected")
    public ResponseEntity<UserDTO> getLoggedInUser(@RequestHeader("Authorization") String authHeader) {
        User user = userService.retrieveLoggedUser(authHeader);
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(new UserDTO(user));
    }
}