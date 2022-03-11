package com.devrezaur.unit.service;

import com.devrezaur.model.Role;
import com.devrezaur.model.User;
import com.devrezaur.repository.UserRepository;
import com.devrezaur.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test Find By Username for Success Case")
    void findUserByUsernameForSuccessCase() {
        User user = new User();
        user.setUserId("11364");
        user.setUsername("rahman.rezaur@bjitgroup.com");
        user.setFullName("Rezaur Rahman");
        user.setRoles(List.of(new Role(2, "ROLE_USER")));

        Mockito.when(userRepository.findByUsernameIgnoreCase("rahman.rezaur@bjitgroup.com")).thenReturn(user);

        User userResponse = userService.findUserByUsername("rahman.rezaur@bjitgroup.com");
        assertNotNull(userResponse);
        assertEquals(userResponse.getUserId(), "11364");
        assertEquals(userResponse.getUsername(), "rahman.rezaur@bjitgroup.com");
        assertEquals(userResponse.getFullName(), "Rezaur Rahman");
        assertEquals(userResponse.getRoles(), List.of(new Role(2, "ROLE_USER")));
    }

    @Test
    @DisplayName("Test Find By Username for Failure Case")
    void findUserByUsernameForFailureCase() {
        User user = new User();
        user.setUserId("11364");
        user.setUsername("rahman.rezaur@bjitgroup.com");
        user.setFullName("Rezaur Rahman");
        user.setRoles(List.of(new Role(2, "ROLE_USER")));

        Mockito.when(userRepository.findByUsernameIgnoreCase("rahman.rezaur@bjitgroup.com")).thenReturn(user);

        User userResponse = userService.findUserByUsername("saleehin@bjitgroup.com");
        assertNull(userResponse);
    }

    @Test
    @DisplayName("Test Find By User ID for Success Case")
    void findUserByUserIdForSuccessCase() {
        User user = new User();
        user.setUserId("11364");
        user.setUsername("rahman.rezaur@bjitgroup.com");
        user.setFullName("Rezaur Rahman");
        user.setRoles(List.of(new Role(2, "ROLE_USER")));

        Mockito.when(userRepository.findUserByUserId("11364")).thenReturn(user);

        User userResponse = userService.findUserByUserId("11364");
        assertNotNull(userResponse);
        assertEquals(userResponse.getUserId(), "11364");
        assertEquals(userResponse.getUsername(), "rahman.rezaur@bjitgroup.com");
        assertEquals(userResponse.getFullName(), "Rezaur Rahman");
        assertEquals(userResponse.getRoles(), List.of(new Role(2, "ROLE_USER")));
    }

    @Test
    @DisplayName("Test Find By User ID for Failure Case")
    void findUserByUserIdForFailureCase() {
        User user = new User();
        user.setUserId("11364");
        user.setUsername("rahman.rezaur@bjitgroup.com");
        user.setFullName("Rezaur Rahman");
        user.setRoles(List.of(new Role(2, "ROLE_USER")));

        Mockito.when(userRepository.findUserByUserId("11364")).thenReturn(user);

        User userResponse = userService.findUserByUserId("11111");
        assertNull(userResponse);
    }

    @Test
    @DisplayName("Test Update By User Info for Success Case")
    void updateUserInfoForSuccessCase() throws Exception {
        User user = new User();
        user.setUserId("11364");
        user.setUsername("rahman.rezaur@bjitgroup.com");
        user.setFullName("Mr Rezaur");
        user.setRoles(List.of(new Role(2, "ROLE_USER")));

        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);

        User userResponse = userService.updateUserInfo(user);
        assertNotNull(userResponse);
        assertEquals(userResponse.getUserId(), "11364");
        assertEquals(userResponse.getUsername(), "rahman.rezaur@bjitgroup.com");
        assertEquals(userResponse.getFullName(), "Mr Rezaur");
        assertEquals(userResponse.getRoles(), List.of(new Role(2, "ROLE_USER")));
    }

    @Test
    @DisplayName("Test Update By User Info for Failure Case")
    void updateUserInfoForFailureCase() {
        User user = new User();
        user.setUserId("11364");
        user.setUsername("rahman.rezaur@bjitgroup.com");
        user.setFullName("Mr Rezaur");
        user.setRoles(List.of(new Role(2, "ROLE_USER")));

        Mockito.when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Failed to add new user"));

        try {
            User userResponse = userService.updateUserInfo(user);
        } catch (Exception ex) {
            assertEquals(ex.getMessage(), "Failed to add new user");
        }
    }

    @Test
    @DisplayName("Test Get Image URL for Success Case")
    void getUserImageForSuccessCase() {
        User user = new User();
        user.setUserId("11364");
        user.setUsername("rahman.rezaur@bjitgroup.com");
        user.setImageUrl("https://devrezaur.com/File-Bucket/image/headshot.jpeg");

        Mockito.when(userRepository.findByUsernameIgnoreCase(any(String.class))).thenReturn(user);

        String imageUrl = userService.getUserImage("rahman.rezaur@bjitgroup.com");
        assertNotNull(imageUrl);
        assertEquals(imageUrl, "https://devrezaur.com/File-Bucket/image/headshot.jpeg");
    }

    @Test
    @DisplayName("Test Update Image URL for Success Case")
    void updateImageUrlForSuccessCase() throws Exception {
        Mockito.doNothing().when(userRepository).updateImageUrl(anyString(), anyString());
        userService.updateImageUrl("11364", "https://devrezaur.com/File-Bucket/image/updated_headshot.jpeg");
    }

    @Test
    @DisplayName("Test Update Image URL for Failure Case")
    void updateImageUrlForFailureCase() {
        Mockito.doThrow(new RuntimeException()).when(userRepository).updateImageUrl(anyString(), anyString());

        try {
            userService.updateImageUrl("11364", "https://devrezaur.com/File-Bucket/image/updated_headshot.jpeg");
        } catch (Exception ex) {
            assertEquals(ex.getMessage(), "Failed to update image url");
        }
    }

    @Test
    @DisplayName("Test Get All User for Success Case")
    void getAllUserForSuccessCase() {
        User user = new User();
        user.setUserId("11364");
        user.setUsername("rahman.rezaur@bjitgroup.com");
        user.setFullName("Rezaur Rahman");
        user.setRoles(List.of(new Role(2, "ROLE_USER")));

        Mockito.when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> userList = userService.getAllUser();
        assertNotNull(userList);
        assertEquals(userList.get(0).getUserId(), "11364");
        assertEquals(userList.get(0).getUsername(), "rahman.rezaur@bjitgroup.com");
        assertEquals(userList.get(0).getFullName(), "Rezaur Rahman");
        assertEquals(userList.get(0).getRoles(), List.of(new Role(2, "ROLE_USER")));
    }
}