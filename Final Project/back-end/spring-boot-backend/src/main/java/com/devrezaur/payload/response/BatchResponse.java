package com.devrezaur.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BatchResponse {

    private int batchId;
    private String batchName;
    private String description;
    private String imageUrl;

}
