package tech.fublog.FuBlog.service;

import tech.fublog.FuBlog.dto.FollowDTO;
import tech.fublog.FuBlog.entity.FollowEntity;
import tech.fublog.FuBlog.model.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tech.fublog.FuBlog.entity.UserEntity;
import tech.fublog.FuBlog.repository.FollowRepository;
import tech.fublog.FuBlog.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FollowService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;

    public ResponseEntity<ResponseObject> followAction(FollowDTO followDTO) {

        Optional<UserEntity> userEntity = userRepository.findById(followDTO.getUserId());
        Optional<UserEntity> followingUserEntity = userRepository.findById(followDTO.getFollowingUserId());
        if (followRepository
                .findFollowerIdAndFollowingId(userEntity.get(), followingUserEntity.get()) < 1) {

            UserEntity user = userEntity.get();
            UserEntity followingUser = followingUserEntity.get();

            FollowEntity newFollowEntity = new FollowEntity(user, followingUser);

            followRepository.save(newFollowEntity);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("success", "followed", ""));
        } else {
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY)
                    .body(new ResponseObject("failed", "Has followed this user", ""));
        }
    }


    public ResponseEntity<ResponseObject> unFollowAction(FollowDTO followDTO) {
        Optional<UserEntity> userEntity = userRepository.findById(followDTO.getUserId());
        Optional<UserEntity> followingUserEntity = userRepository.findById(followDTO.getFollowingUserId());

        if (followRepository
                .findFollowerIdAndFollowingId(userEntity.get(), followingUserEntity.get()) < 1) {

            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY)
                    .body(new ResponseObject("failed", "Has not followed this user", ""));
        } else {


            followRepository.deleteFollowerIdAndFollowingId(userEntity.get(), followingUserEntity.get());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("success", "Unfollowed", ""));
        }
    }

}


