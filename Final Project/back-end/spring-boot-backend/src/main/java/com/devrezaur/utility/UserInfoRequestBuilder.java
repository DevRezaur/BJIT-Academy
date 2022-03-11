package com.devrezaur.utility;

import com.devrezaur.model.Role;
import com.devrezaur.model.User;
import com.devrezaur.payload.request.UserInfoRequest;
import com.devrezaur.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserInfoRequestBuilder {

    @Autowired
    private RoleRepository roleRepository;

    public User build(UserInfoRequest userInfoRequest) {
        Role role = roleRepository.findByRole(userInfoRequest.getRole());

        return new User(userInfoRequest.getUserId(), userInfoRequest.getUsername(), userInfoRequest.getPassword(), List.of(role),
                userInfoRequest.getFullName(), userInfoRequest.getImageUrl(), userInfoRequest.getGender(), userInfoRequest.getContact(),
                userInfoRequest.getPresentAddress(), userInfoRequest.getPermanentAddress(), userInfoRequest.getDepartment());
    }

}
