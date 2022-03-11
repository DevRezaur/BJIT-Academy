package com.devrezaur.utility;

import com.devrezaur.model.User;
import com.devrezaur.payload.response.UserInfoResponse;
import com.devrezaur.payload.response.UserSummaryResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserInfoResponseBuilder {

    public UserInfoResponse build(User user) {
        return new UserInfoResponse(user.getUserId(), user.getUsername(), user.getPassword(), user.getRoles().get(0).getRole(),
                user.getFullName(), user.getImageUrl(), user.getGender(), user.getContact(),
                user.getPresentAddress(), user.getPermanentAddress(), user.getDepartment());
    }

    public List<UserInfoResponse> buildAll(List<User> userList) {
        List<UserInfoResponse> userInfoResponseList = new ArrayList<>();

        for (User user : userList) {
            userInfoResponseList.add(new UserInfoResponse(user.getUserId(), user.getUsername(), user.getPassword(),
                    user.getRoles().get(0).getRole(), user.getFullName(), user.getImageUrl(), user.getGender(),
                    user.getContact(), user.getPresentAddress(), user.getPermanentAddress(), user.getDepartment()));
        }

        return userInfoResponseList;
    }

    public List<UserSummaryResponse> buildAllUserSummary(List<User> userList) {
        List<UserSummaryResponse> userSummaryResponses = new ArrayList<>();

        for (User user : userList) {
            userSummaryResponses.add(new UserSummaryResponse(user.getUserId(), user.getUsername(), user.getRoles().get(0).getRole(),
                    user.getFullName(), user.getImageUrl(), user.getDepartment()));
        }

        return userSummaryResponses;
    }

}
