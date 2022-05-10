package com.example.BlogPostApp;

import com.example.BlogPostApp.model.Role;
import com.example.BlogPostApp.model.User;
import com.example.BlogPostApp.service.CommentService;
import com.example.BlogPostApp.service.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;

@Slf4j
@Component
public class UserCommandLine implements CommandLineRunner {
    @Autowired
    UserServiceImpl userService;
    @Autowired
    CommentService commentService;
    @Override
    public void run(String... args) throws Exception {
        User user=new User("ahmed","firstUser",
                "pass","this is myEmail"
                ,new ArrayList<>(Arrays.asList(new Role("admin"))));
        User user1=new User("mohamed","secondUser",
                "pass","this is myEmail2"
                ,new ArrayList<>(Arrays.asList(new Role("user"))));

//        log.info("comments is in commands {} ",commentService.findCommentsByPostId(new Long(6)).get(0).getText());

//        userService.saveUser(user);
//        userService.saveUser(user1);
    }
}
