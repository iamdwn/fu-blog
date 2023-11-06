package tech.fublog.FuBlog.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.fublog.FuBlog.dto.BlogPostDTO;
import tech.fublog.FuBlog.dto.TagDTO;
import tech.fublog.FuBlog.dto.UserDTO;
import tech.fublog.FuBlog.dto.response.PaginationResponseDTO;
import tech.fublog.FuBlog.dto.response.UserInfoResponseDTO;
import tech.fublog.FuBlog.dto.response.UserRankDTO;
import tech.fublog.FuBlog.entity.*;
import tech.fublog.FuBlog.exception.BlogPostException;
import tech.fublog.FuBlog.exception.UserException;
import tech.fublog.FuBlog.hash.Hashing;
import tech.fublog.FuBlog.model.ResponseObject;
import tech.fublog.FuBlog.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import tech.fublog.FuBlog.Utility.*;


@Service
@Transactional
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final BlogPostRepository blogPostRepository;
    private final VoteRepository voteRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       BlogPostRepository blogPostRepository, VoteRepository voteRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.blogPostRepository = blogPostRepository;
        this.voteRepository = voteRepository;
        this.commentRepository = commentRepository;
    }


    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private Hashing hashing;

    private PasswordEncoder passwordEncoder;


    public UserEntity saveUser(UserEntity user) {
//        String pass = hashing.hasdPassword(user.getHashed_password());
//        user.setHashed_password(pass);
        user.setHashedpassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    public RoleEntity saveRole(RoleEntity role) {
        return null;
    }


    public void addToUser(String username, String rolename) {
        UserEntity user = userRepository.findByUsernameAndStatusTrue(username).get();
        RoleEntity role = roleRepository.findByName(rolename);
        user.getRoles().add(role);
    }


    public ResponseEntity<ResponseObject> getActiveUser() {
        List<UserEntity> userEntities = userRepository.findAllByOrderByPointDesc();
        List<UserInfoResponseDTO> highestPointUser = new ArrayList<>();

        for (UserEntity user : userEntities) {
            if (user.getPoint().equals(userEntities.get(0).getPoint())) {
                UserInfoResponseDTO userInfoResponseDTO =
                        DTOConverter.convertUserDTO(user);

                highestPointUser.add(userInfoResponseDTO);
            }
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new tech.fublog.FuBlog.model.ResponseObject("found", "list found",
                        highestPointUser));
    }


    public UserInfoResponseDTO getUserInfo(Long userId) {
        UserEntity user = userRepository.findByIdAndStatusIsTrue(userId);
        if (user != null) {
            return DTOConverter.convertUserDTO(user);
        } else throw new UserException("");
    }


    public List<UserEntity> getAllUser() {
//        Pageable pageable = PageRequest.of(page,size);
        return userRepository.findAll();
    }

    public PaginationResponseDTO getAllUsers(int page, int size) {
        List<UserInfoResponseDTO> userDTOs = new ArrayList<>();
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<UserEntity> pageResult = userRepository.findAllByStatusIsTrue(pageable);
        for (UserEntity dto : pageResult.getContent()) {
            userDTOs.add(DTOConverter.convertUserDTO(dto));
        }

        Long userCount = pageResult.getTotalElements();
        Long pageCount = (long) pageResult.getTotalPages();
        return new PaginationResponseDTO(userDTOs, userCount, pageCount);
    }

    public PaginationResponseDTO getAllUserByPoint(int page, int size) {
        List<UserRankDTO> userDTOs = new ArrayList<>();
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<UserEntity> pageResult = userRepository.findAllByStatusIsTrueOrderByPointDesc(pageable);
        for (UserEntity dto : pageResult.getContent()) {
            userDTOs.add(DTOConverter.convertUserRankDTO(dto));
        }

        Long userCount = pageResult.getTotalElements();
        Long pageCount = (long) pageResult.getTotalPages();
        return new PaginationResponseDTO(userDTOs, userCount, pageCount);
    }
    public PaginationResponseDTO getAllUserByPoints() {
        List<UserRankDTO> userDTOs = new ArrayList<>();
        List<UserEntity> pageResult = userRepository.findAllByStatusIsTrueOrderByPointDesc();
        for (UserEntity dto : pageResult) {
            userDTOs.add(DTOConverter.convertUserRankDTO(dto));
        }

        return new PaginationResponseDTO(userDTOs, (long) pageResult.size(), 1L);
    }

    public PaginationResponseDTO getAllUserByAward(String award, int page, int size) {
        List<UserRankDTO> userDTOs = new ArrayList<>();
        Long rankPointStart = 0L;
        Long rankPointEnd = 0L;
        Pageable pageable = PageRequest.of(page - 1, size);
        if (award.equalsIgnoreCase("diamond")) {
            rankPointStart = 10000L;
            rankPointEnd = Long.MAX_VALUE;
        } else if (award.equalsIgnoreCase("gold")) {
            rankPointStart = 5000L;
            rankPointEnd = 10000L;
        } else if (award.equalsIgnoreCase("silver")) {
            rankPointStart = 1000L;
            rankPointEnd = 5000L;
        }

        Page<UserEntity> pageResult = userRepository.findAllByStatusIsTrueAndRankPointOrderByPointDesc(rankPointStart, rankPointEnd, pageable);
        for (UserEntity dto : pageResult.getContent()) {
                userDTOs.add(DTOConverter.convertUserRankDTO(dto));
        }

        Long userCount = pageResult.getTotalElements();
        Long pageCount = (long) pageResult.getTotalPages();
        return new PaginationResponseDTO(userDTOs, userCount, pageCount);
    }

    public void markPost(Long userId, Long postId) {
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isPresent()
                && userEntity.get().getStatus()) {
            Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findByIdAndStatusIsTrueAndIsApprovedIsTrue(postId);
            if (blogPostEntity.isPresent()) {
                Set<BlogPostEntity> entitySet;
                if (userEntity.get().getMarkPosts().isEmpty()) {
                    entitySet = new HashSet<>();
                    entitySet.add(blogPostEntity.get());
                    userEntity.get().setMarkPosts(entitySet);
                    userRepository.save(userEntity.get());
                } else {
                    entitySet = userEntity.get().getMarkPosts();
                    if (entitySet.add(blogPostEntity.get())) {
                        userEntity.get().setMarkPosts(entitySet);
                        userRepository.save(userEntity.get());
                    } else throw new UserException("You already marked this post!");
                }
            } else throw new UserException("Blog doesn't exists!");
        }
    }

    public void unMarkPost(Long userId, Long postId) {
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isPresent()
                && userEntity.get().getStatus()) {
            Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findByIdAndStatusIsTrueAndIsApprovedIsTrue(postId);
            if (blogPostEntity.isPresent()) {
                if (!userEntity.get().getMarkPosts().isEmpty()) {
                    userEntity.get().getMarkPosts().removeIf(entity -> entity.getId().equals(postId));
                    userEntity.get().setMarkPosts(userEntity.get().getMarkPosts());
                    userRepository.save(userEntity.get());
                }
            } else throw new UserException("Blog doesn't exists!");
        } else throw new UserException("User doesn't exists");
    }

    public UserEntity getUserById(Long userId) {

        return userRepository.findById(userId).orElse(null);
    }

    public ResponseEntity<ResponseObject> deleteUser(Long userId) {
        Optional<UserEntity> userEntity = userRepository.findById(userId);

        if (userEntity.isPresent()
                && userEntity.get().getStatus()) {
            UserEntity user = this.getUserById(userId);
            user.setStatus(false);
            userRepository.save(user);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("OK", "deleted successful", user));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseObject("Not found", "user not found", ""));
    }

    public ResponseEntity<ResponseObject> updateUser(Long userId, UserDTO userDTO) {

        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isPresent()) {
            if (userDTO.getRole() != null) {
                Set<RoleEntity> roleEntities = new HashSet<>();
                RoleEntity userRole = roleRepository.findByName(userDTO.getRole().toUpperCase());
                roleEntities.add(userRole);

                UserEntity user = this.getUserById(userId);
                user.setFullName(userDTO.getFullName());
                user.setEmail(userDTO.getEmail());
                user.setPicture(userDTO.getPicture());
                user.setRoles(roleEntities);
                user.setStatus(userDTO.getStatus());
                userRepository.save(user);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "updated successful", user));
            } else {
                UserEntity user = this.getUserById(userId);
                user.setFullName(userDTO.getFullName());
                user.setEmail(userDTO.getEmail());
                user.setPicture(userDTO.getPicture());
                user.setStatus(userDTO.getStatus());
                userRepository.save(user);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "updated successful", user));
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseObject("failed", "updated failed", ""));
    }

    public List<BlogPostDTO> getMarkPostByUser(Long userId) {
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isPresent()) {
            Set<BlogPostEntity> entitySet = userEntity.get().getMarkPosts();
            if (!entitySet.isEmpty()) {
                List<BlogPostDTO> dtoList = new ArrayList<>();
                for (BlogPostEntity entity : entitySet) {
                    dtoList.add(DTOConverter.convertPostToDTO(entity.getId()));
                }
                return dtoList;
            } else throw new UserException("This user not marked any post");
        } else throw new UserException("User doesn't exists");
    }

    public PaginationResponseDTO getBlogByBookMarkUser(Long userId, int page, int size) {
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isPresent()) {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<BlogPostEntity> pageList = blogPostRepository.findByUserMarksAndStatusTrueAndIsApprovedTrueOrderByCreatedDateDesc(userEntity.get(), pageable);
            if (!pageList.isEmpty()) {
                List<BlogPostDTO> dtoList = new ArrayList<>();
                for (BlogPostEntity entity : pageList.getContent()) {
                    dtoList.add(DTOConverter.convertPostToDTO(entity.getId()));
                }

                Long blogPostCount = pageList.getTotalElements();
                Long pageCount = (long) pageList.getTotalPages();
                return new PaginationResponseDTO(dtoList, blogPostCount, pageCount);
            } else throw new UserException("This user not marked any post");
        } else throw new UserException("User doesn't exists");
    }

    public Long countViewOfBlog(Long userId, Boolean isCheckAward) {
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        Long count = 0L;
        if (userEntity.isPresent()) {
            Set<BlogPostEntity> entitySet = userEntity.get().getBlogAuthors();
            if (!entitySet.isEmpty()) {
                for (BlogPostEntity entity : entitySet) {
                    count += entity.getView();
                }
                return count;
            } else if(!isCheckAward) throw new UserException("This user not wrote any post");
        } else if(!isCheckAward) throw new UserException("User doesn't exists");
        return count;
    }

    public Long countVoteOfBlog(Long userId, Boolean isCheckAward) {
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        Long count = 0L;
        if (userEntity.isPresent()) {
            Set<BlogPostEntity> entitySet = userEntity.get().getBlogAuthors();
            if (!entitySet.isEmpty()) {
                for (BlogPostEntity entity : entitySet) {
                    count += entity.getVotes().size();
                }
                return count;
            } else if(!isCheckAward) throw new UserException("This user not wrote any post");
        } else if(!isCheckAward) throw new UserException("User doesn't exists");
        return count;
    }

    public void setRole(UserEntity user, String role) {
        RoleEntity roleEntity = roleRepository.findByName(role);
        if (roleEntity != null) {
            Set<RoleEntity> roleEntities = new HashSet<>();
            RoleEntity userRole = roleRepository.findByName(role);
            roleEntities.add(userRole);
            user.setRoles(roleEntities);

        } else throw new UserException("Role doesn't exists!");
    }

    public boolean checkMarkPost(Long userId, Long postId) {
        boolean result = false;
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isPresent()) {
            Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(postId);
            if (blogPostEntity.isPresent()) {
                if (!userEntity.get().getMarkPosts().isEmpty()) {
                    for (BlogPostEntity entity : userEntity.get().getMarkPosts()) {
                        if (entity.getId().equals(blogPostEntity.get().getId()))
                            result = true;
                    }
                }
                return result;
            } else throw new UserException("Blog doesn't exists!");
        } else throw new UserException("User doesn't exists!");
    }



}
