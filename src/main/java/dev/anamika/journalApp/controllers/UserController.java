package dev.anamika.journalApp.controllers;

import dev.anamika.journalApp.models.Users;
import dev.anamika.journalApp.services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("v1/update")
    public ResponseEntity<Users> updateUser(@RequestBody Users u){
        String userName = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        Users user = userService.updateUser(u, userName);
        if (user != null){
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("v1/delete")
    public ResponseEntity<?> deleteUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        String userName = auth.getName();

        boolean deleted = userService.deleteUser(userName);
        if (deleted) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
