package com.devrezaur.service;

import com.devrezaur.model.Post;
import com.devrezaur.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<Post> getAllPostByBatchId(int batchId) {
        List<Post> posts = postRepository.findAllByBatchId(batchId);

        if (posts != null)
            Collections.reverse(posts);

        return posts;
    }

    public boolean createPost(Post post) {
        return (postRepository.save(post) != null) ? true : false;
    }
}
