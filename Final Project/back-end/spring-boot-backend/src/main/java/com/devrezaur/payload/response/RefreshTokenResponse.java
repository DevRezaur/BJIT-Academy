package com.devrezaur.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RefreshTokenResponse {

    private String tokenType;
    private String accessToken;
    private String refreshToken;

}
