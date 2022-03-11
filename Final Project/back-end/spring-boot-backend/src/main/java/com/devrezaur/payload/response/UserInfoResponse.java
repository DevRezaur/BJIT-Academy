package com.devrezaur.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserInfoResponse {

    private String userId;
    private String username;
    private String password;
    private String role;
    private String fullName;
    private String imageUrl;
    private String gender;
    private String contact;
    private String presentAddress;
    private String permanentAddress;
    private String department;

}
