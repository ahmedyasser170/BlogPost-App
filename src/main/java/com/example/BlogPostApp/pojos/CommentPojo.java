package com.example.BlogPostApp.pojos;

import lombok.Data;

@Data
public class CommentPojo {

    private String text;
    private Long postId;


}