package tech.fublog.FuBlog.service;

import tech.fublog.FuBlog.dto.request.FollowRequestDTO;
import tech.fublog.FuBlog.dto.response.FollowResponseDTO;
import tech.fublog.FuBlog.entity.FollowEntity;
import tech.fublog.FuBlog.entity.UserEntity;
import tech.fublog.FuBlog.exception.FollowException;
import tech.fublog.FuBlog.repository.FollowRepository;
import tech.fublog.FuBlog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class FollowService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    @Autowired
    public FollowService(UserRepository userRepository, FollowRepository followRepository) {
        this.userRepository = userRepository;
        this.followRepository = followRepository;
    }

    public void insertFollow(FollowRequestDTO followRequestDTO) {
        Optional<UserEntity> userFollower = userRepository.findById(followRequestDTO.getFollower());
        Optional<UserEntity> userFollowing = userRepository.findById(followRequestDTO.getFollowing());
        if (userFollower.isPresent() && userFollowing.isPresent()) {
            FollowEntity followEntity = followRepository.findByFollowerAndFollowing(userFollower.get(), userFollowing.get());
            if (followEntity == null) {
                followEntity = new FollowEntity(userFollower.get(), userFollowing.get());
                followRepository.save(followEntity);
            } else throw new FollowException("You already follow this user");
        } else throw new FollowException("User doesn't exists");
    }

    public List<FollowResponseDTO> viewFollower(Long userId) {
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isPresent()) {
            Set<FollowEntity> set = userEntity.get().getFollowingList();
            if (!set.isEmpty()) {
                List<FollowResponseDTO> dtoList = new ArrayList<>();
                for (FollowEntity entity : set) {
                    FollowResponseDTO dto = new FollowResponseDTO(entity.getFollower().getId(), entity.getFollowing().getId());
                    dtoList.add(dto);
                }
                return dtoList;
            } else throw new FollowException("List empty");
        } else throw new FollowException("User doesn't exists");
    }

    public List<FollowResponseDTO> viewFollowing(Long userId) {
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isPresent()) {
            Set<FollowEntity> set = userEntity.get().getFollowersList();
            if (!set.isEmpty()) {
                List<FollowResponseDTO> dtoList = new ArrayList<>();
                for (FollowEntity entity : set) {
                    FollowResponseDTO dto = new FollowResponseDTO(entity.getFollower().getId(), entity.getFollowing().getId());
                    dtoList.add(dto);
                }
                return dtoList;
            } else throw new FollowException("List empty");
        } else throw new FollowException("User doesn't exists");
    }

    public void unFollow(FollowRequestDTO followRequestDTO) {
        Optional<UserEntity> userFollower = userRepository.findById(followRequestDTO.getFollower());
        Optional<UserEntity> userFollowing = userRepository.findById(followRequestDTO.getFollowing());
        if (userFollower.isPresent() && userFollowing.isPresent()) {
            FollowEntity followEntity = followRepository.findByFollowerAndFollowing(userFollower.get(), userFollowing.get());
            if (followEntity != null) {
                followRepository.delete(followEntity);
            } else throw new FollowException("You hasn't follow this user");
        } else throw new FollowException("User doesn't exists");
    }

    public Long countFollower(Long userId) {
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isPresent()) {
            Long count = followRepository.countByFollowing(userEntity.get());
            return count;
        } else throw new FollowException("User doesn't exists");
    }

    public Long countFollowing(Long userId) {
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isPresent()) {
            Long count = followRepository.countByFollower(userEntity.get());
            return count;
        } else throw new FollowException("User doesn't exists");
    }
}
