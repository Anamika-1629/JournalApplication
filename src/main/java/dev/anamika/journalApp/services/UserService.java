package dev.anamika.journalApp.services;

import dev.anamika.journalApp.dto.*;
import dev.anamika.journalApp.models.Users;
import dev.anamika.journalApp.repositories.JournalEntryRepository;
import dev.anamika.journalApp.repositories.UserRepository;
import dev.anamika.journalApp.utils.JwtUtils;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    //Public controller -> signup api
    public void registerUser(UserRegistration dto){
        Users user = new Users();

        user.setUserName(dto.getUserName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setRoles(List.of("USER"));

        userRepository.save(user);
    }

    //Public controller -> login api
    public String login(@Valid AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
        return jwtUtils.generateToken(request.getUserName());
    }

    //Save user method
    public void saveUser(Users user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of("USER"));
        userRepository.save(user);
    }

    //User controller -> update user api
    public Users updateUser(UpdateUser dto, String username){
        Users user = userRepository.findByUserName(username).orElse(null);
        if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");

        if (dto.getUserName() != null && !dto.getUserName().isEmpty()) user.setUserName(dto.getUserName());
        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) user.setEmail(dto.getEmail());
        if (dto.getFirstName() != null && !dto.getFirstName().isEmpty()) user.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null && !dto.getLastName().isEmpty()) user.setLastName(dto.getLastName());

        userRepository.save(user);
        return user;
    }

    //User controller -> change password api
    public void changePassword(ChangePassword dto, String username){
        Users user = userRepository.findByUserName(username).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword()))
            throw new BadCredentialsException("Old password is incorrect");

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

    //User controller -> delete user api
    public void deleteUser(String userName){
        Users user = userRepository.findByUserName(userName).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));

        journalEntryRepository.deleteAll(user.getEntryIDs());
        userRepository.delete(user);
    }

    //Admin controller -> gives list of all users
    public List<UserResponse> getAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private UserResponse mapToResponse(Users user){
        return new UserResponse(
                user.getUserName(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRoles()
        );
    }

    //Admin controller -> update role of the users
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

    public Optional<Users> findByUsername(String userName){
        return userRepository.findByUserName(userName);
    }

    public Optional<Users> getOneUser(ObjectId id){
        return userRepository.findById(id);
    }
}
