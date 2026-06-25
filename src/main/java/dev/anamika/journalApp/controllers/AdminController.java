package dev.anamika.journalApp.controllers;

import dev.anamika.journalApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("v1/find-all")
    public ResponseEntity<List<Map<String, Object>>> findAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @PutMapping("v1/users/{username}/update-role")
    public ResponseEntity<?> updateRoles(@RequestBody List<String> roles, @PathVariable String username){
        System.out.println("Update role endpoint hit");
        return ResponseEntity.ok(userService.updateRoles(username, roles));
    }
}
