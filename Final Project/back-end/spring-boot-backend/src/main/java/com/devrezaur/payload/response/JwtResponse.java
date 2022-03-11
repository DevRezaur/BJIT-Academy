package com.devrezaur.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class JwtResponse {

    private String type;
    private String token;
    private String refreshToken;
    private String userId;
    private String username;
    private String fullName;
    private List<String> roles;

}
