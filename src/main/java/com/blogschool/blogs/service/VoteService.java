package com.blogschool.blogs.service;

import com.blogschool.blogs.dto.VoteDTO;
import com.blogschool.blogs.entity.*;
import com.blogschool.blogs.repository.BlogPostRepository;
import com.blogschool.blogs.repository.UserRepository;
import com.blogschool.blogs.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final BlogPostRepository blogPostRepository;
    private final UserRepository userRepository;

    @Autowired
    public VoteService(VoteRepository voteRepository, BlogPostRepository blogPostRepository, UserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.blogPostRepository = blogPostRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<ResponseObject> viewVote(Long postId) {
        Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(postId);
        if (blogPostEntity.isPresent()) {
            List<VoteEntity> list = voteRepository.findByPostVote(blogPostEntity.get());
            if (list.size() > 0) {
                List<VoteDTO> dtoList = new ArrayList<>();
                for (VoteEntity entity : list) {
                    VoteDTO dto = new VoteDTO(entity.getVoteId(), entity.getVoteValue(), entity.getPostVote().getPostId(), entity.getUserVote().getUserId());
                    dtoList.add(dto);
                }
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "votes of postId: " + postId, dtoList));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseObject("not found", "no votes found of postId: " + postId, ""));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("failed", "blog doesn't exists", ""));
        }
    }

    public ResponseEntity<ResponseObject> upsertVote(VoteDTO voteDTO) {
        Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(voteDTO.getPostId());
        Optional<UserEntity> userEntity = userRepository.findById(voteDTO.getUserId());
        if (blogPostEntity.isPresent() && userEntity.isPresent()) {
//            BlogPostEntity blogPost = blogPostEntity.get();
//            UserEntity user = userEntity.get();
            VoteEntity voteEntity = voteRepository.findByUserVoteAndPostVote(userEntity.get(), blogPostEntity.get());
//            Optional<VoteEntity> voteEntity = voteRepository.findById(voteDTO.getVoteId());
            if (voteEntity != null) {
//                VoteEntity updateVote = voteEntity.get();
//                updateVote.setVoteValue(voteDTO.getVoteValue());
                voteEntity.setVoteValue(voteDTO.getVoteValue());
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "vote have been updated", voteRepository.save(voteEntity)));
            } else {
                voteEntity = new VoteEntity(voteDTO.getVoteValue(), userEntity.get(), blogPostEntity.get());
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "vote have been inserted", voteRepository.save(voteEntity)));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("failed", "user or blogpost doesn't exists", ""));
        }
    }

//    public ResponseEntity<ResponseObject> updateVote(Long voteId, VoteDTO voteDTO) {
//        Optional<VoteEntity> voteEntity = voteRepository.findById(voteId);
//        if (voteEntity.isPresent()) {
//            VoteEntity updateVote = voteEntity.get();
//            updateVote.setVoteValue(voteDTO.getVoteValue());
//            return ResponseEntity.status(HttpStatus.OK)
//                    .body(new ResponseObject("ok", "update vote successfully", voteRepository.save(updateVote)));
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new ResponseObject("failed", "vote doesn't exists", ""));
//        }
//    }
}
