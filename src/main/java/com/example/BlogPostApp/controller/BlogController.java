package com.example.BlogPostApp.controller;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.BlogPostApp.model.Comment;
import com.example.BlogPostApp.model.Post;
import com.example.BlogPostApp.model.User;
import com.example.BlogPostApp.pojos.CommentPojo;
import com.example.BlogPostApp.service.*;
import com.example.BlogPostApp.utils.JwtUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Date;
import java.util.List;
@Slf4j
@RestController
public class BlogController {
    @Autowired
    PostServiceImpl postService;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    CommentService commentService;

    @GetMapping("/posts")
    public List<Post> getPosts() {
        return postService.getPosts();
    }
    @PostMapping("/post/save")
    public ResponseEntity<Post> savePost(@RequestBody Post post) {
        if(post.getDateCreated()==null)
            post.setDateCreated(new Date());
        String userName=SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        post.setCreator(userService.getUser(userName));
        URI uri=URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        return ResponseEntity.created(uri).body(postService.savePost(post));
    }
    @GetMapping(value="/posts/{username}")
    public List<Post> postsByUser(@PathVariable String username){
        return postService.findByUser(userService.getUser(username));
    }
    @GetMapping("/the_post/{id}")
    public Post postById(@PathVariable("id") int postId) {
        log.info("here is post id : {}",postId);
        return postService.findById(postId);
    }
    @GetMapping(value = "/the_post/{postId}/comments")
    public List<Comment> getComments(@PathVariable Long postId){
        log.info("comments is {} ",commentService.findCommentsByPostId(postId).get(0).getText());
        return commentService.findCommentsByPostId(postId);
    }
    @PostMapping("/post/comment")
    public Comment CommentBypostId(@RequestBody CommentPojo commentPojo)
    {

        Post post = postService.findById(commentPojo.getPostId());
        String userName=SecurityContextHolder.getContext().getAuthentication().getName();
        User user=userService.getUser(userName);
        Comment comment = new Comment(commentPojo.getText(),post,user);
        if(post == null || user == null)
            return null;

        return commentService.addComment(comment);
    }


}
