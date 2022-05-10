package com.example.BlogPostApp.service;

import com.example.BlogPostApp.model.Post;
import com.example.BlogPostApp.model.User;
import com.example.BlogPostApp.repository.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PostServiceImpl implements PostService{
    @Autowired
    private PostRepo postRepo;

    @Override
    public Post savePost(Post post) {
        return postRepo.save(post);
    }

    @Override
    public List<Post> getPosts() {
        return postRepo.findAll();
    }

    @Override
    public List<Post> findByUser(User user) {
        return postRepo.findByCreatorId(user.getId());
    }

    @Override
    public Post findById(long id) {
        return postRepo.findById(id).get();
    }

}
