package com.blogschool.blogs.service;

import com.blogschool.blogs.dto.CommentDTO;
import com.blogschool.blogs.dto.VoteDTO;
import com.blogschool.blogs.entity.*;
import com.blogschool.blogs.exception.BlogPostException;
import com.blogschool.blogs.exception.VoteException;
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

    public Long countVote(Long postId) {
        Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(postId);
        if (blogPostEntity.isPresent()) {
            List<VoteEntity> entity = voteRepository.findByPostVote(blogPostEntity.get());
            Long count = 0L;
            if (!entity.isEmpty()) {
                for (VoteEntity voteEntity : entity) {
                    if (voteEntity.getVoteValue() == 1) {
                        count++;
                    } else if (voteEntity.getVoteValue() == -1) {
                        count--;
                    }
                }
            }
            return count;
        } else throw new VoteException("Blog doesn't exists");
    }

    public List<VoteDTO> viewVote(Long postId) {
        Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(postId);
        if (blogPostEntity.isPresent()) {
            List<VoteEntity> list = voteRepository.findByPostVote(blogPostEntity.get());
            if (!list.isEmpty()) {
                List<VoteDTO> dtoList = new ArrayList<>();
                for (VoteEntity entity : list) {
                    VoteDTO dto = new VoteDTO(entity.getId(), entity.getVoteValue(), entity.getPostVote().getId(), entity.getUserVote().getId());
                    dtoList.add(dto);
                }
                return dtoList;
            } else throw new VoteException("List empty");
        } else throw new VoteException("Blog doesn't exists");
    }

    public VoteDTO insertVote(VoteDTO voteDTO) {
        Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(voteDTO.getPostId());
        Optional<UserEntity> userEntity = userRepository.findById(voteDTO.getUserId());
        if (blogPostEntity.isPresent() && userEntity.isPresent()) {
            VoteEntity voteEntity = voteRepository.findByUserVoteAndPostVote(userEntity.get(), blogPostEntity.get());
            if (voteEntity != null) {
                voteRepository.delete(voteEntity);
                return null;
            } else {
                voteEntity = new VoteEntity(voteDTO.getVoteValue(), userEntity.get(), blogPostEntity.get());
                voteRepository.save(voteEntity);
                return voteDTO;
            }
        } else throw new VoteException("User or Blog doesn't exists");
    }
}
