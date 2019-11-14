package com.springrobotwebapp.app.controller;

import com.springrobotwebapp.app.model.User;
import com.springrobotwebapp.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin("http://localhost:4200")
public class UserController {

    public final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public void addUser(@RequestBody User user){
        userRepository.save(user);
    }

    @GetMapping
    public List<User> getUsers(){
        return userRepository.findAll();
    }

}
