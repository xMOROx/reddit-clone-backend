package com.example.backendclonereddit.services;

import com.example.backendclonereddit.entities.User;
import com.example.backendclonereddit.repositories.UserRepository;
import com.example.backendclonereddit.utils.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("id-" + id));
    }

    public User createNewUser(User user) {
        userRepository.save(user);
        return user;
    }

    public void remove(Long id) {
        userRepository.deleteById(id);
    }

    public Long fullUpdate(Long id, User user) {
        User userToUpdate;
        try {
            userToUpdate =  getUserById(id);
        } catch (UserNotFoundException e) {
            createNewUser(user);
            userToUpdate = user;
        }

        userToUpdate.setId(id);
        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setPassword(user.getPassword());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setComments(user.getComments());
        userToUpdate.setPosts(user.getPosts());
        userToUpdate.setVotes(user.getVotes());

        userRepository.save(userToUpdate);

        return userToUpdate.getId();
    }

    public Long partialUpdate(Long id, User user) {
        User userToUpdate = getUserById(id);

        if (user.getUsername() != null) {
            userToUpdate.setUsername(user.getUsername());
        }
        if (user.getPassword() != null) {
            userToUpdate.setPassword(user.getPassword());
        }
        if (user.getEmail() != null) {
            userToUpdate.setEmail(user.getEmail());
        }

        if (user.getComments() != null) {
            userToUpdate.setComments(user.getComments());
        }

        if (user.getPosts() != null) {
            userToUpdate.setPosts(user.getPosts());
        }

        if (user.getVotes() != null) {
            userToUpdate.setVotes(user.getVotes());
        }

        userRepository.save(userToUpdate);

        return userToUpdate.getId();
    }



}
