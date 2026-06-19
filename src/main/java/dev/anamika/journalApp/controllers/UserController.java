package dev.anamika.journalApp.controllers;

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

    @PutMapping("v1/update")
    public ResponseEntity<Users> updateUser(@RequestBody Users u, Authentication authentication){
        String userName = authentication.getName();
        Users user = userService.updateUser(u, userName);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("v1/delete")
    public ResponseEntity<?> deleteUser(Authentication authentication){
        userService.deleteUser(authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
