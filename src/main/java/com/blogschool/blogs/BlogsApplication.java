package com.blogschool.blogs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.blogschool"})
public class BlogsApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogsApplication.class, args);
    }
}
