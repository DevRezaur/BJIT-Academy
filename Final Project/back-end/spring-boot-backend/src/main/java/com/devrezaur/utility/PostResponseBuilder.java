package com.devrezaur.utility;

import com.devrezaur.model.Post;
import com.devrezaur.model.User;
import com.devrezaur.payload.response.PostResponse;
import com.devrezaur.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class PostResponseBuilder {

    @Autowired
    private UserService userService;

    public List<PostResponse> build(List<Post> posts) {
        List<PostResponse> postResponseList = new ArrayList<>();
        User user = null;

        for (Post post : posts) {
            user = userService.findUserByUserId(post.getUserId());

            long postId = post.getPostId();
            String fullName = user.getFullName();
            String imageUrl = user.getImageUrl();
            String desc = post.getDescription();
            String resourceName = post.getResourceName();
            String resourcesLink = post.getResourcesLink();
            Date createDate = post.getCreateDate();

            postResponseList.add(new PostResponse(postId, fullName, imageUrl, desc, resourceName, resourcesLink, createDate));
        }

        return postResponseList;
    }
}
