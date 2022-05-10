package com.example.BlogPostApp.service;

import com.example.BlogPostApp.model.Comment;
import com.example.BlogPostApp.model.Post;

import java.util.List;

public interface CommentService {
    List<Comment> findCommentsByPostId(Long postId);
    Comment addComment(Comment comment1);
    void deleteComment(Long commentId);
}
