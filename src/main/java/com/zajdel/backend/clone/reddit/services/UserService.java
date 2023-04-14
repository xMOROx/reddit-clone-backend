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
    /**
     * Get all users
     * @return List of all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    /**
     * Get user by id
     * @param id User id
     * @return User
     * @throws UserNotFoundException if user with given id does not exist
     */
    public User getUserById(Long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("id-" + id));
    }
    /**
     * Create new user if user with given username or email does not exist, otherwise throw UserAlreadyExistsException
     * @param user User to be created
     * @return User
     * @throws UserNotFoundException if user with given username does not exist
     */
    public Long createNewUser(User user) throws UserAlreadyExistsException {
        Optional<User> userByUsername = userRepository.findUserByUsername(user.getUsername());
        Optional<User> userByEmail = userRepository.findUserByEmail(user.getEmail());

        if (userByUsername.isPresent()) {
            throw new UserAlreadyExistsException("User with username: " + user.getUsername() + " already exists");
        }

        if (userByEmail.isPresent()) {
            throw new UserAlreadyExistsException("User with email: "+ user.getEmail() + " already exists");
        }

        userRepository.save(user);
        return user.getId();
    }
    /**
     * Delete user by id
     * @param id User id
     * @throws UserNotFoundException if user with given id does not exist
     */
    public void removeUserById(Long id) throws UserNotFoundException {
        getUserById(id);
        userRepository.deleteById(id);
    }
    /**
     * Update user by id. If user with given id does not exist, create new user
     * @param id User id
     * @param user User to be updated
     * @return User id
     * @throws UserNotFoundException if user with given id does not exist
     */
    public Long fullUpdateUserById(Long id, User user) {
        User userToUpdate;
        try {
            userToUpdate =  getUserById(id);
            userToUpdate.setId(id);
        } catch (UserNotFoundException e) {
            Long createdId  = createNewUser(user);
            userToUpdate = user;
            userToUpdate.setId(createdId);
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
    /**
     * Partial update user by id. If user with given id does not exist, throw UserNotFoundException
     * @param id User id
     * @param user User to be updated
     * @return User id
     * @throws UserNotFoundException if user with given id does not exist
     */
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
