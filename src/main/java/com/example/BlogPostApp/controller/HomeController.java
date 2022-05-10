package com.example.BlogPostApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HomeController {

    @GetMapping(value = "/home")
    public String index() {
        return "index";
    }
    @GetMapping(value="post/{id}")
    public String singlePost(){
        return "post";
    }
}
