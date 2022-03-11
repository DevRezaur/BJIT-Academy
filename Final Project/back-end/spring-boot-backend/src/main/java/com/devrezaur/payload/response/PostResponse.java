package com.devrezaur.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostResponse {

    private long postId;
    private String fullName;
    private String imageUrl;
    private String desc;
    private String resourceName;
    private String resourcesLink;
    private Date createDate;

}
