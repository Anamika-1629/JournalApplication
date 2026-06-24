package dev.anamika.journalApp.services;

import dev.anamika.journalApp.models.Users;
import dev.anamika.journalApp.repositories.JournalEntryRepository;
import dev.anamika.journalApp.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Map<String, Object>> getAllUsers(){
        List<Users> users = userRepository.findAll();

        return users.stream().map(user -> {
            Map<String, Object> map = new HashMap<>();
            map.put("userName", user.getUserName());
            map.put("roles", user.getRoles());
            map.put("journalCount", user.getEntryIDs().size());
            return map;
        }).toList();
    }

    public Optional<Users> getOneUser(ObjectId id){
        return userRepository.findById(id);
    }

    public void saveUser(Users user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of("USER"));
        userRepository.save(user);
    }

    public Users updateUser(Users u, String username){
        Users user = userRepository.findByUserName(username).orElse(null);
        if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");

        if (u.getUserName() != null && !u.getUserName().isEmpty()) user.setUserName(u.getUserName());
        if (u.getPassword() != null && !u.getPassword().isEmpty()) user.setPassword(passwordEncoder.encode(u.getPassword()));

        userRepository.save(user);
        return user;
    }

    public void deleteUser(String userName){
        Users user = userRepository.findByUserName(userName).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));

        journalEntryRepository.deleteAll(user.getEntryIDs());
        userRepository.delete(user);
    }

    public Optional<Users> findByUsername(String userName){
        return userRepository.findByUserName(userName);
    }

    public boolean updateRoles(String username, List<String> roles){
        Users user = userRepository.findByUserName(username).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.getRoles().contains("ADMIN") && !roles.contains("ADMIN"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot remove admin role from admin");

        if (!roles.stream().allMatch(
                role -> role.equals("USER") || role.equals("ADMIN")))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role");

        if (roles.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role list can't be empty");

        user.setRoles(roles);
        userRepository.save(user); return true;
    }
}
