package com.devrezaur.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserSummaryResponse {

    private String userId;
    private String username;
    private String role;
    private String fullName;
    private String imageUrl;
    private String department;

}
