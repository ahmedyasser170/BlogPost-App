package com.example.BlogPostApp.service;

import com.example.BlogPostApp.model.Comment;
import com.example.BlogPostApp.model.Post;
import com.example.BlogPostApp.repository.CommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    CommentRepo commentRepo;

    @Override
    public List<Comment> findCommentsByPostId(Long postId) {
        return commentRepo.findByPostId(postId);
    }

    @Override
    public Comment addComment(Comment comment1) {
        return commentRepo.save(comment1);
    }

    @Override
    public void deleteComment(Long commentId) {
        Comment comment=commentRepo.getById(commentId);
        commentRepo.delete(comment);
    }
}
