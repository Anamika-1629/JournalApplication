package dev.anamika.journalApp.controllers;

import dev.anamika.journalApp.models.Users;
import dev.anamika.journalApp.services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("v1/create-user")
    public ResponseEntity<Users> createUser(@RequestBody Users newUser){
        userService.saveUser(newUser);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @GetMapping("v1/find-all")
    public ResponseEntity<List<Users>> findAllUsers(){
        List<Users> all = userService.getAllUsers();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @GetMapping("v1/find-one/{id}")
    public ResponseEntity<Optional<Users>> findOneUser(@PathVariable ObjectId id){
        Optional<Users> user = userService.getOneUser(id);
        if (user.isPresent()){
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("v1/update/{userName}")
    public ResponseEntity<Users> updateUser(@PathVariable String userName, @RequestBody Users u){
        Users user = userService.findByUsername(userName).orElse(null);
        if (user != null){
            user.setUserName(!u.getUserName().isEmpty() ? u.getUserName() : user.getUserName());
            user.setPassword(!u.getPassword().isEmpty() ? u.getPassword() : user.getPassword());

            userService.saveUser(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("v1/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable ObjectId id){
        if (userService.getOneUser(id).isPresent()) {
            userService.delUser(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
