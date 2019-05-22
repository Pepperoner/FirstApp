package com.project.app.services;

import com.project.app.entities.User;
import com.project.app.repositories.UserRepository;
import com.project.app.exceptions.UserNameExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser(User newUser){
        try{
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
            newUser.setUsername(newUser.getUsername());
            return userRepository.save(newUser);
        }catch (Exception e){
            throw new UserNameExistsException("Username '"+newUser.getUsername()+"' already exists");
        }
    }
}
