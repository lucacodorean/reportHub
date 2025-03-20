package com.reporthub.controller.api.v1;

import com.reporthub.dto.UserDTO;
import com.reporthub.entity.User;
import com.reporthub.request.api.v1.UserUpdateRequest;
import com.reporthub.service.IMailService;
import com.reporthub.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("/api/v1/users")
public class UserController {

    @Autowired private IUserService userService;

    @Autowired private IMailService mailService;

    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> index() {
        return ResponseEntity.status(HttpStatus.OK).body(
                userService.findAll().stream().map(UserDTO::new).toList()
        );
    }

    @GetMapping("/{key}")
    public ResponseEntity<UserDTO> get(@PathVariable String key) {

        User temp = userService.findByKey(key);
        if(temp == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.status(HttpStatus.OK).body(new UserDTO(temp));
    }

    @PatchMapping("/{key}")
    @PreAuthorize("@authorizationService.canEditUser(authentication.principal.id, #key)")
    public ResponseEntity<UserDTO> update(@PathVariable String key, @RequestBody UserUpdateRequest request) {
        User user = userService.findByKey(key);
        if(user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        if(request.getUsername() != null)       user.setUsername(request.getUsername());
        if(request.getPhoneNumber() != null)    user.setPhoneNumber(request.getPhoneNumber());
        if(request.getEmail() != null)          user.setEmail(request.getEmail());
        if(request.getScore() != null)          user.setScore(request.getScore());
        if(request.getBanned() != null && user.getIsBanned() != request.getBanned())  {
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
        if(request.getPassword() != null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
            user.setPassword(encoder.encode(request.getPassword()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(
            new UserDTO(userService.save(user))
        );
    }

    @DeleteMapping("/{key}")
    @PreAuthorize("@authorizationService.canDeleteUser(authentication.principal.id, #key)")
    public ResponseEntity<Boolean> delete(@PathVariable String key) {
        Boolean status = userService.delete(userService.findByKey(key));
        return ResponseEntity.status(HttpStatus.OK).body(status);
    }
}
