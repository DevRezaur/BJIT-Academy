package com.devrezaur.service;

import com.devrezaur.model.User;
import com.devrezaur.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findUserByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username.toLowerCase());
    }

    public User findUserByUserId(String userId) {
        return userRepository.findUserByUserId(userId);
    }

    public User updateUserInfo(User user) throws Exception {
        try {
            return userRepository.save(user);
        } catch (Exception ex) {
            throw new Exception("Failed to add new user");
        }
    }

    public String getUserImage(String username) {
        return findUserByUsername(username).getImageUrl();
    }

    public void updateImageUrl(String userId, String imageUrl) throws Exception {
        try {
            userRepository.updateImageUrl(userId, imageUrl);
        } catch (Exception ex) {
            throw new Exception("Failed to update image url");
        }
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }
}
