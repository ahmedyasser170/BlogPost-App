package com.example.BlogPostApp.controller;

import com.example.BlogPostApp.model.User;
import com.example.BlogPostApp.pojos.*;
import com.example.BlogPostApp.service.UserServiceImpl;
import com.example.BlogPostApp.utils.JwtUtility;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/getUserName")
    public String getUserName(HttpServletRequest request) {
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        return username;
    }
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }
    @PostMapping("/user/save")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        if(userService.getUser(user.getUserName())!=null)
            return ResponseEntity.badRequest().build();
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }
    @PostMapping("/role/save")
    public ResponseEntity<?> saveUser(@RequestBody RoleToUserForm roleToUserForm) {
        userService.addRoleToUser(roleToUserForm.getUsername(),roleToUserForm.getRoleName());
        return ResponseEntity.ok().build();
    }
    @GetMapping("/token/refresh")
    public void checkRefreshAndRetrieveAccess(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authoriztionHeader=request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authoriztionHeader!=null && authoriztionHeader.startsWith("Bearer ")) {
            try {
                String refresh_token=authoriztionHeader.substring("Bearer ".length());
                String userName=JwtUtility.getUserName(refresh_token);
                User user=userService.getUser(userName);
                String access_token= JwtUtility.generateAccessToken(user,request);
                Map<String,String> map = JwtUtility.generateMap(refresh_token,access_token);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),map);

            } catch (Exception e) {
                log.error("error is {} ",e.getMessage());
                response.setHeader("error",e.getMessage());
                response.setStatus(HttpStatus.FORBIDDEN.value());
                Map<String,String> map=new HashMap<>();
                map.put("error",e.getMessage());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),map);
            }
        } else {
            throw new RuntimeException("Refresh Token is missing");
        }

    }



}


