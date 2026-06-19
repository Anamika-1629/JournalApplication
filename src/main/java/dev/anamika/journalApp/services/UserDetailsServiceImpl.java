package dev.anamika.journalApp.services;

import dev.anamika.journalApp.models.Users;
import dev.anamika.journalApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByUserName(username).orElseThrow(() -> new UsernameNotFoundException("No user found with the username: "+ username));

        return User.builder()
                .username(user.getUserName())
                .password(user.getPassword())
                .roles(user.getRoles().toArray(new String[0]))
                .build();
    }
}
