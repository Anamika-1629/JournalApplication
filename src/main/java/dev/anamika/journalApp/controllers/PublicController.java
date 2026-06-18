package dev.anamika.journalApp.controllers;

import dev.anamika.journalApp.models.Users;
import dev.anamika.journalApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;

    @GetMapping("/v1/health-check")
    public String healthCheck(){
        return "Ok";
    }

    @PostMapping("/v1/create-user")
    public ResponseEntity<Users> createUser(@RequestBody Users newUser){
        userService.saveUser(newUser);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }
}
