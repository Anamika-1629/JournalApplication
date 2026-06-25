package dev.anamika.journalApp.controllers;

import dev.anamika.journalApp.dto.ChangePassword;
import dev.anamika.journalApp.dto.UpdateUser;
import dev.anamika.journalApp.dto.UserResponse;
import dev.anamika.journalApp.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PatchMapping("v1/update")
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UpdateUser dto, Authentication authentication){
        UserResponse user  = userService.updateUser(dto, authentication.getName());
        return ResponseEntity.ok(user);
    }

    @PatchMapping("v1/password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePassword dto, Authentication authentication){
        userService.changePassword(dto, authentication.getName());
        return ResponseEntity.ok("Password updated successfully");
    }

    @DeleteMapping("v1/delete")
    public ResponseEntity<?> deleteUser(Authentication authentication){
        userService.deleteUser(authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
