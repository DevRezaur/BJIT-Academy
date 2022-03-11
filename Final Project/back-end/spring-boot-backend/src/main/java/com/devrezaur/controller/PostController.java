package com.devrezaur.controller;

import com.devrezaur.model.Post;
import com.devrezaur.service.PostService;
import com.devrezaur.utility.PostResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/post")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private PostResponseBuilder postResponseBuilder;


    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/{batchId}")
    public ResponseEntity<?> getAllPosts(@PathVariable int batchId) {
        List<Post> postList = postService.getAllPostByBatchId(batchId);

        if (postList.size() == 0)
            return ResponseEntity.status(404).body("Post Not Found");

        return ResponseEntity.status(200).body(postResponseBuilder.build(postList));
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody Post post) {
        if (postService.createPost(post))
            return ResponseEntity.status(201).body("Successfully Post Created");

        return ResponseEntity.status(400).body("Failed to Create Post");
    }
}
