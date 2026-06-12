package dev.anamika.journalApp.services;

import dev.anamika.journalApp.models.Users;
import dev.anamika.journalApp.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<Users> getAllUsers(){
        return userRepository.findAll();
    }

    public Optional<Users> getOneUser(ObjectId id){
        return userRepository.findById(id);
    }

    public void saveUser(Users user){
        userRepository.save(user);
    }

    public void delUser(ObjectId id){
        userRepository.deleteById(id);
    }

    public Optional<Users> findByUsername(String userName){
        return userRepository.findByUserName(userName);
    }
}
