package com.OnlineMarketplace.OnlineMarketplace.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public User updateUser(Long id, User user){
        if (userRepository.existsById(id)){
            User existingUser = userRepository.findById(id).get();
            if (user.getUserName() != null) {
                existingUser.setUserName(user.getUserName());
            }
            if (user.getUserSurname() != null) {
                existingUser.setUserSurname(user.getUserSurname());
            }
            if (user.getEmail() != null) {
                existingUser.setEmail(user.getEmail());
            }
            return userRepository.save(existingUser);
        }else {
            return null;
        }
    }
}
