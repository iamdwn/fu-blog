package com.blogschool.blogs.controller;

import com.blogschool.blogs.dto.FollowDTO;
import com.blogschool.blogs.entity.ResponseObject;
import com.blogschool.blogs.exception.FollowException;
import com.blogschool.blogs.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogPosts/user")
public class FollowController {
    private final FollowService followService;

    @Autowired
    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @GetMapping("/follower/{userId}")
    public ResponseEntity<ResponseObject> viewFollower(@PathVariable Long userId) {
        try {
            List<FollowDTO> dtoList = followService.viewFollower(userId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "found", dtoList));
        } catch (FollowException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/following/{userId}")
    public ResponseEntity<ResponseObject> viewFollowing(@PathVariable Long userId) {
        try {
            List<FollowDTO> dtoList = followService.viewFollowing(userId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "found", dtoList));
        } catch (FollowException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/follower/count/{userId}")
    public ResponseEntity<ResponseObject> countFollower(@PathVariable Long userId) {
        try {
            Long count = followService.countFollower(userId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "found", count));
        } catch (FollowException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/following/count/{userId}")
    public ResponseEntity<ResponseObject> countFollowing(@PathVariable Long userId) {
        try {
            Long count = followService.countFollowing(userId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "found", count));
        } catch (FollowException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @PostMapping("/follow/{action}")
    public ResponseEntity<ResponseObject> insertFollow(@PathVariable String action, @RequestBody FollowDTO followDTO) {
        try {
            if (action.equals("follow"))
                followService.insertFollow(followDTO);
            else if (action.equals("unfollow"))
                followService.unFollow(followDTO);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "successfully", ""));
        } catch (FollowException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }
}
