package com.example.backendclonereddit.resources;

import com.example.backendclonereddit.repositories.UserRepository;
import com.example.backendclonereddit.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserResource {

    private final UserService userService;
    private final UserRepository userRepository;

//    -------------------  PATHS  -------------------
    private static final String path = "/api/v1/users";
    private final static String pathId = "/api/v1/users/{id}";
    private final String pathEmail = "/api/v1/users/{email}";
//    -------------------  PATHS  -------------------

    public UserResource(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping(path = path)
    public String getAllUsers() {
        return "All users";
    }

    @GetMapping(path = pathId)
    public String getUserById(@PathVariable String id) {
        return "User by id";
    }

}
