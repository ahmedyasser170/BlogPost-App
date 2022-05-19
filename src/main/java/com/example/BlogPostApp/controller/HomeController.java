package com.example.BlogPostApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping(value = "/")
    public String basic() {
        return "redirect:home";
    }
    @GetMapping(value = "/home")
    public String home() {
        return "index";
    }
    @GetMapping(value="post/{id}")
    public String singlePost(){
        return "post";
    }
}
