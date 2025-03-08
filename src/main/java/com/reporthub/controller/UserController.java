package com.reporthub.controller;

import com.reporthub.dto.UserDTO;
import com.reporthub.entity.User;
import com.reporthub.service.IUserService;
import com.reporthub.singleton.ServiceSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping ("/api/v1/users")
public class UserController {

    @Autowired
    private IUserService userService = ServiceSingleton.getUserService();

    /*
        index -> get All GET
        api/v1/users/usr_.... -> un user GET
        api/v1/users/usr_.... -> PATCH
        api/v1/users/usr_.... -> DELETE
     */

    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> index() {
        return ResponseEntity.status(HttpStatus.OK).body(
                userService.findAll().stream().map(UserDTO::new).toList()
        );
    }

    @GetMapping("/{key}")
    public ResponseEntity<UserDTO> get(@PathVariable String key) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new UserDTO(userService.findByKey(key))
        );
    }
}
