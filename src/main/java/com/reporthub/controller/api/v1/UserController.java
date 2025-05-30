package com.reporthub.controller.api.v1;

import com.reporthub.dto.UserDTO;
import com.reporthub.request.api.v1.UserUpdateRequest;
import com.reporthub.service.IUserService;
import com.reporthub.exception.NotFoundException;
import com.reporthub.service.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("/api/v1/users")
public class UserController {

    @Autowired private IUserService userService;

    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> index() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.all());
    }

    @GetMapping("/{key}")
    public ResponseEntity<UserDTO> get(@PathVariable String key) {
        Response<UserDTO> myResponse = userService.retrieveDTO(key);
        if(myResponse.getEntityDTO() != null) return ResponseEntity.ok(myResponse.getEntityDTO());
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PatchMapping("/{key}")
    @PreAuthorize("@authorizationService.canEditUser(authentication.principal.id, #key)")
    public ResponseEntity<?> update(@PathVariable String key, @RequestBody UserUpdateRequest request) {
        Response<UserDTO> myResponse = userService.update(key, request);
        if(myResponse.getEntityDTO() != null) return ResponseEntity.ok(myResponse.getEntityDTO());
        else return ResponseEntity.badRequest().body(myResponse.retrieveMessages());
    }

    @DeleteMapping("/{key}")
    @PreAuthorize("@authorizationService.canDeleteUser(authentication.principal.id, #key)")
    public ResponseEntity<Boolean> delete(@PathVariable String key) {
        try {
            Boolean status = userService.delete(userService.findByKey(key));
            return ResponseEntity.status(HttpStatus.OK).body(status);
        } catch (NotFoundException ex) { return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); }
    }
}
