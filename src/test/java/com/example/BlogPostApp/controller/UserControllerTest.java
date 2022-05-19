package com.example.BlogPostApp.controller;

import com.example.BlogPostApp.model.Role;
import com.example.BlogPostApp.model.User;
import com.example.BlogPostApp.pojos.RoleToUserForm;
import com.example.BlogPostApp.service.UserServiceImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ActiveProfiles("test")
@WebMvcTest(value = UserController.class)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;
    Gson gson ;

    @MockBean
    UserServiceImpl userService;
    @MockBean
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    public void before() {
        gson=new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    @Test
    @WithMockUser(username = "ahmed12",password = "sdsds",authorities = {"admin"})
    void shouldReturnListOfUsers() throws Exception {
        User user=new User("ahmed","a1","test"
                ,"email",new ArrayList<>(Arrays.asList(new Role("user"))));
        User user1=new User("mohamed","a1","test"
                ,"email",new ArrayList<>(Arrays.asList(new Role("user"))));

        Mockito.when(userService.getUsers()).thenReturn(List.of(user,user1));
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/users").accept(MediaType.APPLICATION_JSON);
        MvcResult result=mockMvc.perform(requestBuilder).andReturn();

        String expected = gson.toJson(List.of(user,user1));
        JSONAssert.assertEquals(expected,result.getResponse().getContentAsString(),false);
    }

    @Test
    @WithMockUser(username = "ahmed312",password = "sdsds",authorities = {"admin"})
    void shouldReturnUserName() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/getUserName").accept(MediaType.APPLICATION_JSON);
        MvcResult result=mockMvc.perform(requestBuilder).andReturn();
        String expected = "ahmed312";
        Assertions.assertEquals(expected,result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "ahmed15",password = "sdsds",authorities = {"admin"})
    void shouldSaveUser() throws Exception {
        User user=new User("ahmed","a1","test"
                ,"email",new ArrayList<>(Arrays.asList(new Role("user"))));

        Mockito.when(userService.getUser(Mockito.anyString()))
                .thenReturn(null);
        Mockito.when(userService.saveUser(user))
                .thenReturn(user);
        gson=new Gson();
        String bodyContent=gson.toJson(user);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/user/save").content(bodyContent)
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);
        MvcResult result=mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

    }
    @Test
    @WithMockUser(username = "ahmed15",password = "sdsds",authorities = {"admin"})
    void shouldAddRole() throws Exception {
        RoleToUserForm roleToUserForm=new RoleToUserForm();
        roleToUserForm.setRoleName("admin2");
        roleToUserForm.setUsername("ahmed");
        Mockito.doNothing().when(userService).addRoleToUser(Mockito.anyString(),Mockito.anyString());
        gson=new Gson();
        String bodyContent=gson.toJson(roleToUserForm);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/role/save").content(bodyContent)
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);
        MvcResult result=mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }
    @Test
    @WithMockUser(username = "ahmed12",password = "sdsds",authorities = {"user"})
    void shouldReturnListOfUsersForbidden() throws Exception {
        User user=new User("ahmed","a1","test"
                ,"email",new ArrayList<>(Arrays.asList(new Role("user"))));
        User user1=new User("mohamed","a1","test"
                ,"email",new ArrayList<>(Arrays.asList(new Role("user"))));

        Mockito.when(userService.getUsers()).thenReturn(List.of(user,user1));
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/users").accept(MediaType.APPLICATION_JSON);
        MvcResult result=mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isForbidden()).andReturn();
    }
    @Test
    @WithMockUser(username = "ahmed15",password = "sdsds",authorities = {"user"})
    void shouldAddRoleForbiddenRequest() throws Exception {
        RoleToUserForm roleToUserForm=new RoleToUserForm();
        roleToUserForm.setRoleName("admin2");
        roleToUserForm.setUsername("ahmed");
        Mockito.doNothing().when(userService).addRoleToUser(Mockito.anyString(),Mockito.anyString());
        gson=new Gson();
        String bodyContent=gson.toJson(roleToUserForm);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/role/save").content(bodyContent)
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);
        MvcResult result=mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isForbidden()).andReturn();
    }
    @Test
    @WithMockUser(username = "ahmed15",password = "sdsds",authorities = {"user"})
    void shouldSaveUserForbidden() throws Exception {
        User user=new User("ahmed","a1","test"
                ,"email",new ArrayList<>(Arrays.asList(new Role("user"))));

        Mockito.when(userService.getUser(Mockito.anyString()))
                .thenReturn(null);
        Mockito.when(userService.saveUser(user))
                .thenReturn(user);
        gson=new Gson();
        String bodyContent=gson.toJson(user);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/user/save").content(bodyContent)
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);
        MvcResult result=mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isForbidden()).andReturn();

    }
}
