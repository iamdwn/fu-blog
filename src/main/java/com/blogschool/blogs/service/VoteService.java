package com.blogschool.blogs.service;

import com.blogschool.blogs.dto.VoteDTO;
import com.blogschool.blogs.entity.BlogPostEntity;
import com.blogschool.blogs.entity.UserEntity;
import com.blogschool.blogs.entity.VoteEntity;
import com.blogschool.blogs.model.ResponseObject;
import com.blogschool.blogs.repository.BlogPostRepository;
import com.blogschool.blogs.repository.UserRepository;
import com.blogschool.blogs.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final BlogPostRepository blogPostRepository;
    private final UserRepository userRepository;

    @Autowired
    public VoteService(VoteRepository votesRepository, BlogPostRepository blogPostRepository, UserRepository userRepository) {
        this.voteRepository = votesRepository;
        this.blogPostRepository = blogPostRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<ResponseObject> viewVotes(Long postId) {

        Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(postId);
        if (blogPostEntity.isPresent()) {

            List<VoteEntity> list = voteRepository.findByPostVote(blogPostEntity.get());
            return list.size() > 0 ?
                    ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseObject("ok", "votes of postId: " + postId, list)) :
                    ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponseObject("failed", "no votes found of postId: " + postId, ""));
        } else {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("failed", "blog doesn't exists", ""));
        }
    }

    public ResponseEntity<ResponseObject> insertVotes(Long postId, VoteDTO voteDTO) {

        Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(postId);
        Optional<UserEntity> userEntity = userRepository.findById(voteDTO.getUserId());

        if (blogPostEntity.isPresent() && userEntity.isPresent()) {

            VoteEntity voteEntity = new VoteEntity(voteDTO.getVoteValue(), userEntity.get(), blogPostEntity.get());

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "vote have been inserted", voteRepository.save(voteEntity)));
        } else {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("failed", "vote can not insert", ""));
        }
    }
}
