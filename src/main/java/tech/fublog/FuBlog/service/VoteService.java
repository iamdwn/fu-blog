package tech.fublog.FuBlog.service;

import tech.fublog.FuBlog.dto.VoteDTO;
import tech.fublog.FuBlog.entity.*;
import tech.fublog.FuBlog.exception.VoteException;
import tech.fublog.FuBlog.model.ResponseObject;
import tech.fublog.FuBlog.repository.BlogPostRepository;
import tech.fublog.FuBlog.repository.UserRepository;
import tech.fublog.FuBlog.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tech.fublog.FuBlog.entity.BlogPostEntity;
import tech.fublog.FuBlog.entity.UserEntity;

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
                Double point = userEntity.get().getPoint();
                userEntity.get().setPoint(point + 0.5);
                voteEntity = new VoteEntity(voteDTO.getVoteValue(), userEntity.get(), blogPostEntity.get());
                voteRepository.save(voteEntity);
                return voteDTO;
            }
        } else throw new VoteException("User or Blog doesn't exists");
    }
}
