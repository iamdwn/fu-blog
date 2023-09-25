package tech.fublog.FuBlog.controller;

import tech.fublog.FuBlog.dto.FollowDTO;
import tech.fublog.FuBlog.entity.UserEntity;
import tech.fublog.FuBlog.model.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.fublog.FuBlog.service.FollowService;
import tech.fublog.FuBlog.repository.UserRepository;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth/user")
@CrossOrigin(origins = "*")
public class FollowController {

    @Autowired
    private FollowService followService;

    @PostMapping("/follow")
    ResponseEntity<ResponseObject> follow(@RequestBody FollowDTO followDTO) {
        if (followDTO.getAction().equalsIgnoreCase("follow")) {

            return followService.followAction(followDTO);
        } else {

            return followService.unFollowAction(followDTO);
        }
    }

}