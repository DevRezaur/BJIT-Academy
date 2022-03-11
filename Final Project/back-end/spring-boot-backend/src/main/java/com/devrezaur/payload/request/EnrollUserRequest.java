package com.devrezaur.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EnrollUserRequest {

    private int batchId;
    private String userId;

}
