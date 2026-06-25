package dev.anamika.journalApp.controllers;

import dev.anamika.journalApp.dto.ChangePassword;
import dev.anamika.journalApp.dto.UpdateUser;
import dev.anamika.journalApp.models.Users;
import dev.anamika.journalApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PatchMapping("v1/update")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUser dto, Authentication authentication){
        Users user = userService.updateUser(dto, authentication.getName());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PatchMapping("v1/password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePassword dto, Authentication authentication){
        userService.changePassword(dto, authentication.getName());
        return ResponseEntity.ok("Password updated successfully");
    }

    @DeleteMapping("v1/delete")
    public ResponseEntity<?> deleteUser(Authentication authentication){
        userService.deleteUser(authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
