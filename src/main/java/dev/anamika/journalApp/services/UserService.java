package dev.anamika.journalApp.services;

import dev.anamika.journalApp.dto.*;
import dev.anamika.journalApp.exception.InvalidCredentialsException;
import dev.anamika.journalApp.exception.InvalidRoleOperationException;
import dev.anamika.journalApp.exception.ResourceNotFoundException;
import dev.anamika.journalApp.models.Users;
import dev.anamika.journalApp.repositories.JournalEntryRepository;
import dev.anamika.journalApp.repositories.UserRepository;
import dev.anamika.journalApp.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
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
    @Transactional
    public void registerUser(UserRegistration dto){
        Users user = new Users();

        user.setUserName(dto.getUserName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setRoles(List.of("USER"));

        userRepository.save(user);
        log.info("User '{}' registered successfully", user.getUserName());
    }

    //Public controller -> login api
    public String login(AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
            log.info("User '{}' logged in successfully", request.getUserName());
            return jwtUtils.generateToken(request.getUserName());
        }
        catch (BadCredentialsException e){
            log.warn("Failed login attempt for the user '{}': invalid credentials", request.getUserName());
            throw new InvalidCredentialsException("Invalid username or password");
        }
    }

    //Save user method
    public void saveExistingUser(Users user){
        userRepository.save(user);
    }

    //User controller -> update user api
    @Transactional
    public UserResponse updateUser(UpdateUser dto, String username) {
        Users user = userRepository.findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        if (dto.getUserName() != null && !dto.getUserName().isBlank())
            user.setUserName(dto.getUserName());
        if (dto.getEmail() != null && !dto.getEmail().isBlank())
            user.setEmail(dto.getEmail());
        if (dto.getFirstName() != null && !dto.getFirstName().isBlank())
            user.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null && !dto.getLastName().isBlank())
            user.setLastName(dto.getLastName());

        userRepository.save(user);
        log.info("User '{}' updated their profile", username);
        return mapToResponse(user);
    }

    //User controller -> change password api
    public void changePassword(ChangePassword dto, String username){
        Users user = userRepository.findByUserName(username).orElseThrow(()-> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())){
            log.warn("User '{}' provided incorrect old password during password change", username);
            throw new InvalidCredentialsException("Old password is incorrect");}

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        log.info("User '{}' changed their password successfully", username);
        userRepository.save(user);
    }

    //User controller -> delete user api
    @Transactional
    public void deleteUser(String userName){
        Users user = userRepository.findByUserName(userName).orElseThrow(()-> new ResourceNotFoundException("User Not Found"));

        journalEntryRepository.deleteAll(user.getEntryIDs());
        userRepository.delete(user);
        log.info("User '{}' deleted their account", userName);
    }

    //Admin controller -> gives list of all users
    public List<UserResponse> getAllUsers(){
        log.info("Providing user list to the admin");
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
    @Transactional
    public UserResponse updateRoles(String username, List<String> roles){
        Users user = userRepository.findByUserName(username).orElseThrow(()-> new ResourceNotFoundException("User not found"));

        if (user.getRoles().contains("ADMIN") && !roles.contains("ADMIN")){
            log.warn("User '{}' is already an Admin, cannot remove Admin role from an Admin", username);
            throw new InvalidRoleOperationException("Cannot remove admin role from admin");
        }

        if (!roles.stream().allMatch(
                role -> role.equals("USER") || role.equals("ADMIN"))){
            log.warn("Invalid role update attempted: {}", roles);
            throw new InvalidRoleOperationException("Invalid role");
        }

        if (roles.isEmpty()) throw new InvalidRoleOperationException("Role list can't be empty");

        user.setRoles(roles);
        Users updatedUser = userRepository.save(user);
        log.info("Role updated for the user '{}'", username);
        return mapToResponse(updatedUser);
    }

    public Optional<Users> findByUsername(String userName){
        return userRepository.findByUserName(userName);
    }
}
