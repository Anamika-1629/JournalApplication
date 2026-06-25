package dev.anamika.journalApp.controllers;

import dev.anamika.journalApp.dto.AuthRequest;
import dev.anamika.journalApp.dto.UserRegistration;
import dev.anamika.journalApp.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/public")
public class PublicController {


    @Autowired
    private UserService userService;

    @GetMapping("/v1/health-check")
    public String healthCheck(){
        return "Ok";
    }

    @PostMapping("/v1/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody UserRegistration newUser){
        userService.registerUser(newUser);
        return new ResponseEntity<>("New User Created", HttpStatus.CREATED);
    }

    @PostMapping("/v1/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
        String token = userService.login(request);
        return ResponseEntity.ok(Map.of("token", token));
    }
}
