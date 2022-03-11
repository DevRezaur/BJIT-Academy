package com.devrezaur.controller;

import com.devrezaur.model.User;
import com.devrezaur.payload.request.UserInfoRequest;
import com.devrezaur.service.UserService;
import com.devrezaur.utility.UserInfoRequestBuilder;
import com.devrezaur.utility.UserInfoResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserInfoRequestBuilder userInfoRequestBuilder;
    @Autowired
    private UserInfoResponseBuilder userInfoResponseBuilder;

    /*
     * Profile Related APIs
     */

    @Secured({"ROLE_ADMIN", "ROLE_USER" })
    @GetMapping("/profile")
    public ResponseEntity<?> getPersonalInfo(Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
        return ResponseEntity.status(200).body(userInfoResponseBuilder.build(user));
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER" })
    @PostMapping("/profile/update")
    public ResponseEntity<?> updatePersonalInfo(Principal principal, @RequestBody UserInfoRequest userInfoRequest) {
        if (!principal.getName().equals(userInfoRequest.getUsername()))
            return ResponseEntity.status(401).body("Unauthorized request!");

        try {
            User user = userInfoRequestBuilder.build(userInfoRequest);
            user = userService.updateUserInfo(user);
            return ResponseEntity.status(200).body(userInfoResponseBuilder.build(user));
        } catch (Exception ex) {
            return ResponseEntity.status(400).body(ex.getMessage());
        }
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER" })
    @GetMapping("profile/imageUrl")
    public ResponseEntity<?> getImageUrl(Principal principal) {
        return ResponseEntity.status(200).body(userService.getUserImage(principal.getName()));
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER" })
    @PostMapping("/profile/imageUrl/update")
    public ResponseEntity<?> updateImageUrl(Principal principal, @RequestBody UserInfoRequest userInfoRequest) {
        if (!principal.getName().equals(userInfoRequest.getUsername()))
            return ResponseEntity.status(401).body("Unauthorized request!");

        try {
            userService.updateImageUrl(userInfoRequest.getUserId(), userInfoRequest.getImageUrl());
        } catch (Exception ex) {
            return ResponseEntity.status(400).body(ex.getMessage());
        }
        return ResponseEntity.status(200).body("Image Info Updated Successfully");
    }

    /*
     * User Management Related APIs
     */

    @Secured("ROLE_ADMIN")
    @PostMapping("/add")
    public ResponseEntity<?> addNewUserData(@RequestBody UserInfoRequest userInfoRequest) {
        try {
            User user = userInfoRequestBuilder.build(userInfoRequest);
            user = userService.updateUserInfo(user);
            return ResponseEntity.status(201).body(userInfoResponseBuilder.build(user));
        } catch (Exception ex) {
            return ResponseEntity.status(400).body(ex.getMessage());
        }
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/profile/{userId}")
    public ResponseEntity<?> getUserInfoById(@PathVariable String userId) {
        User user = userService.findUserByUserId(userId);
        if (user == null) return ResponseEntity.status(200).body(null);
        return ResponseEntity.status(200).body(userInfoResponseBuilder.build(user));
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/update")
    public ResponseEntity<?> updateExistingUserData(@RequestBody UserInfoRequest userInfoRequest) {
        try {
            User user = userInfoRequestBuilder.build(userInfoRequest);
            user = userService.updateUserInfo(user);
            return ResponseEntity.status(200).body(userInfoResponseBuilder.build(user));
        } catch (Exception ex) {
            return ResponseEntity.status(400).body(ex.getMessage());
        }
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN" })
    @GetMapping("/all")
    public ResponseEntity<?> listAllUsers() {
        return ResponseEntity.status(200).body(userInfoResponseBuilder.buildAllUserSummary(userService.getAllUser()));
    }

}
