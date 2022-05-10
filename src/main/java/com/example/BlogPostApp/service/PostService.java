package com.example.BlogPostApp.service;

import com.example.BlogPostApp.model.Post;
import com.example.BlogPostApp.model.User;

import java.util.List;

public interface PostService {
    Post savePost(Post post);
    List<Post> getPosts();
    List<Post> findByUser(User user);
    Post findById(long id);
}
