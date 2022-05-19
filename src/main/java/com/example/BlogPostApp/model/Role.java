package com.example.BlogPostApp.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Role {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String name;

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

}
