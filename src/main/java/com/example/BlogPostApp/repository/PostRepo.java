package com.example.BlogPostApp.repository;

import com.example.BlogPostApp.model.Post;
import com.example.BlogPostApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepo extends JpaRepository<Post,Long> {
    List<Post> findByCreatorId(Long id);
}
