package com.reporthub.controller.api.auth;

import com.reporthub.dto.UserDTO;
import com.reporthub.request.api.auth.UserLoginRequest;
import com.reporthub.request.api.auth.UserRegisterRequest;
import com.reporthub.service.IUserService;
import com.reporthub.service.Response;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.JDBCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/auth/")
public class AuthController {

    @Autowired
    private IUserService userService;

    @ExceptionHandler({SQLException.class, JDBCException.class})
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterRequest request, HttpServletResponse response)  {
        Response<UserDTO> myResponse = userService.create(request, response);
        if(myResponse.getEntityDTO() != null) return ResponseEntity.ok(myResponse.getEntityDTO());
        else return ResponseEntity.badRequest().body(myResponse.retrieveMessages());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest request, HttpServletResponse response) {
        Response<UserDTO> myResponse = userService.login(request, response);
        if(myResponse.getEntityDTO() != null) return ResponseEntity.ok(myResponse.getEntityDTO());
        else return ResponseEntity.badRequest().body(myResponse.retrieveMessages());
    }

    @PostMapping("/logout")
    @PreAuthorize("@authorizationService.isConnected(authentication.principal.id)")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        if(!userService.logout(response)) return ResponseEntity.internalServerError().build();

        Map<String, String> message = new HashMap<>();
        message.put("message", "User has been logged out.");
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }
}