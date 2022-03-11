package com.devrezaur.unit.service;

import com.devrezaur.model.Post;
import com.devrezaur.repository.PostRepository;
import com.devrezaur.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class PostServiceTest {

    @Mock
    private PostRepository postRepository;
    @InjectMocks
    private PostService postService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test Get All Posts For Success Case")
    void getAllPostByBatchIdForSuccessCase() {
        Post post = new Post();
        post.setPostId(5);
        post.setBatchId(1);
        post.setUserId("11364");
        post.setDescription("This is first demo post.");

        Mockito.when(postRepository.findAllByBatchId(1)).thenReturn(List.of(post));

        List<Post> postList = postService.getAllPostByBatchId(1);
        assertNotNull(postList);
        assertEquals(5, postList.get(0).getPostId());
        assertEquals(1, postList.get(0).getBatchId());
        assertEquals("11364", postList.get(0).getUserId());
        assertEquals("This is first demo post.", postList.get(0).getDescription());
    }

    @Test
    @DisplayName("Test Get All Posts For Null Case")
    void getAllPostByBatchIdForNullCase() {
        Mockito.when(postRepository.findAllByBatchId(1)).thenReturn(null);

        List<Post> postList = postService.getAllPostByBatchId(1);
        assertNull(postList);
    }

    @Test
    @DisplayName("Test Create Post For Success Case")
    void createPostForSuccessCase() {
        Mockito.when(postRepository.save(any(Post.class))).thenReturn(new Post());

        boolean isPostCreated = postService.createPost(new Post());
        assertTrue(isPostCreated);
    }

    @Test
    @DisplayName("Test Create Post For Failure Case")
    void createPostForFailureCase() {
        Mockito.when(postRepository.save(any(Post.class))).thenReturn(null);

        boolean isPostCreated = postService.createPost(new Post());
        assertFalse(isPostCreated);
    }
}