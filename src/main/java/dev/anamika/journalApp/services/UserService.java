package dev.anamika.journalApp.services;

import dev.anamika.journalApp.models.Users;
import dev.anamika.journalApp.repositories.JournalEntryRepository;
import dev.anamika.journalApp.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Users> getAllUsers(){
        return userRepository.findAll();
    }

    public Optional<Users> getOneUser(ObjectId id){
        return userRepository.findById(id);
    }

    public void saveUser(Users user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER"));
        userRepository.save(user);
    }

    public Users updateUser(Users u, String username){
        Users user = userRepository.findByUserName(username).orElse(null);
        if (user != null){
            user.setUserName(!u.getUserName().isEmpty() ? u.getUserName() : user.getUserName());
            user.setPassword(!u.getPassword().isEmpty() ? u.getPassword() : user.getPassword());
            userRepository.save(user);
            return user;
        }
        return null;
    }

    public boolean deleteUser(String userName){
        Users user = userRepository.findByUserName(userName).orElse(null);
        if (user == null) return false;

        journalEntryRepository.deleteAll(user.getEntryIDs());
        userRepository.delete(user);
        return true;
    }

    public Optional<Users> findByUsername(String userName){
        return userRepository.findByUserName(userName);
    }
}
