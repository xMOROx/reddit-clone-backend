package com.zajdel.backend.clone.reddit.services;

import com.zajdel.backend.clone.reddit.entities.User;
import com.zajdel.backend.clone.reddit.repositories.UserRepository;
import com.zajdel.backend.clone.reddit.utils.exceptions.types.UserAlreadyExistsException;
import com.zajdel.backend.clone.reddit.utils.exceptions.types.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public User createNewUser(User user) throws UserAlreadyExistsException {
        Optional<User> userByUsername = userRepository.findUserByUsername(user.getUsername());
        Optional<User> userByEmail = userRepository.findUserByEmail(user.getEmail());
        if (userByUsername.isPresent()) {
            throw new UserAlreadyExistsException("User with username: " + user.getUsername() + " already exists");
        }

        if (userByEmail.isPresent()) {
            throw new UserAlreadyExistsException("User with email: "+ user.getEmail() + " already exists");
        }

        userRepository.save(user);
        return user;
    }

    public void removeUserById(Long id) throws UserNotFoundException {
        getUserById(id);
        userRepository.deleteById(id);
    }

    public Long fullUpdateUserById(Long id, User user) {
        User userToUpdate;
        try {
            userToUpdate =  getUserById(id);
        } catch (UserNotFoundException e) {
            createNewUser(user);
            userToUpdate = user;
        }

        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setPassword(user.getPassword());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setComments(user.getComments());
        userToUpdate.setPosts(user.getPosts());
        userToUpdate.setVotes(user.getVotes());
        userToUpdate.setReplies(user.getReplies());

        userRepository.save(userToUpdate);

        return userToUpdate.getId();
    }

    public Long partialUpdateUserById(Long id, User user) throws UserNotFoundException {
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

        userRepository.save(userToUpdate);

        return userToUpdate.getId();
    }

}
