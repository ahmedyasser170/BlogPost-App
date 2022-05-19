package com.example.BlogPostApp.controller;

import com.example.BlogPostApp.model.Comment;
import com.example.BlogPostApp.model.Post;
import com.example.BlogPostApp.model.Role;
import com.example.BlogPostApp.model.User;
import com.example.BlogPostApp.service.CommentServiceImpl;
import com.example.BlogPostApp.service.PostServiceImpl;
import com.example.BlogPostApp.service.UserServiceImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@WebMvcTest(BlogController.class)
public class PostControllerTest {
    @Autowired
    MockMvc mockMvc;
    Gson gson ;
    @MockBean
    PostServiceImpl postService;
    @MockBean
    UserServiceImpl userService;
    @MockBean
    CommentServiceImpl commentService;
    @MockBean
    BCryptPasswordEncoder passwordEncoder;
    User user;
    Post post;
    @BeforeEach
    public void before() {
        gson=new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        user=new User("ahmed","a1","test"
                ,"email",new ArrayList<>(Arrays.asList(new Role("user"))));
        post=new Post("testTitle","testBody",new Date(),false,user);
    }
    @Test
    void shouldReturnListOfPosts() throws Exception {
        Post post1=new Post("testTitle1","testBody1",new Date(),false,user);
        Mockito.when(postService.getPosts()).thenReturn(List.of(post,post1));
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/posts").accept(MediaType.APPLICATION_JSON);
        MvcResult result=mockMvc.perform(requestBuilder).andReturn();

        String expected = gson.toJson(List.of(post,post1));
        JSONAssert.assertEquals(expected,result.getResponse().getContentAsString(),false);
    }

    @Test
    @WithMockUser(username = "ahmed15",password = "sdsds",authorities = {"admin"})
    void shouldSavePost() throws Exception {

        Mockito.when(userService.getUser(Mockito.anyString()))
                .thenReturn(user);
        Mockito.when(postService.savePost(post))
                .thenReturn(post);
        gson=new Gson();
        String bodyContent=gson.toJson(user);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/post/save").content(bodyContent)
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);
        MvcResult result=mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();
    }
    @Test
    @WithMockUser(username = "ahmed15",password = "sdsds",authorities = {"user"})
    void shouldSavePostForbidden() throws Exception {

        Mockito.when(userService.getUser(Mockito.anyString()))
                .thenReturn(user);
        Mockito.when(postService.savePost(post))
                .thenReturn(post);
        gson=new Gson();
        String bodyContent=gson.toJson(user);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/post/save").content(bodyContent)
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);
        MvcResult result=mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isForbidden()).andReturn();
    }
    @Test
    @WithMockUser(username = "ahmed15",password = "sdsds",authorities = {"user"})
    void shouldFindByUser() throws Exception {

        Mockito.when(postService.findByUser(Mockito.any()))
                .thenReturn(List.of(post));
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/posts/a1").accept(MediaType.APPLICATION_JSON);
        MvcResult result=mockMvc.perform(requestBuilder).andReturn();
        String expected = gson.toJson(List.of(post));
        JSONAssert.assertEquals(expected,result.getResponse().getContentAsString(),false);
    }

    @Test
    void shouldFindPostById() throws Exception {

        Mockito.when(postService.findById(Mockito.anyLong()))
                .thenReturn(post);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/the_post/6").accept(MediaType.APPLICATION_JSON);
        MvcResult result=mockMvc.perform(requestBuilder).andReturn();
        String expected = gson.toJson(post);
        JSONAssert.assertEquals(expected,result.getResponse().getContentAsString(),false);
    }

    @Test
    void shouldFindCommentsByPostId() throws Exception {
        Comment comment=new Comment("sfdsf",null,null);

        Mockito.when(commentService.findCommentsByPostId(Mockito.anyLong()))
                .thenReturn(List.of(comment));
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/the_post/6/comments").accept(MediaType.APPLICATION_JSON);
        MvcResult result=mockMvc.perform(requestBuilder).andReturn();
        String expected = gson.toJson(List.of(comment));
        JSONAssert.assertEquals(expected,result.getResponse().getContentAsString(),false);
    }

}
