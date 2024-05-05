package dev.ayush.userservice.services;

import dev.ayush.userservice.exceptions.UserNotFoundException;
import dev.ayush.userservice.models.User;
import dev.ayush.userservice.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // get user by id
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    // add new user
    public User addUser(User user) {
        return userRepository.save(user);
    }

    // update user
    // update user
    public User replaceUser(Long id, User newUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(newUser.getName());
                    user.setEmail(newUser.getEmail());
                    user.setUsername(newUser.getUsername());
                    user.setHashedPassword(newUser.getHashedPassword());
                    user.setAddress(newUser.getAddress());
                    user.setPhone(newUser.getPhone());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    // patch user
    public User updateUser(Long id, User newUser) {
        return userRepository.findById(id)
                .map(user -> {
                    if (newUser.getName() != null) {
                        user.setName(newUser.getName());
                    }
                    if (newUser.getEmail() != null) {
                        user.setEmail(newUser.getEmail());
                    }
                    if (newUser.getUsername() != null) {
                        user.setUsername(newUser.getUsername());
                    }
                    if (newUser.getHashedPassword() != null) {
                        user.setHashedPassword(newUser.getHashedPassword());
                    }
                    if (newUser.getAddress() != null) {
                        user.setAddress(newUser.getAddress());
                    }
                    if (newUser.getPhone() != null) {
                        user.setPhone(newUser.getPhone());
                    }
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    // delete user
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        userRepository.delete(user);
    }
}
