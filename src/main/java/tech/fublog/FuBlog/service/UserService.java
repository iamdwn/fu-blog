package tech.fublog.FuBlog.service;

import tech.fublog.FuBlog.entity.BlogPostEntity;
import tech.fublog.FuBlog.entity.UserEntity;
import tech.fublog.FuBlog.exception.UserException;
import tech.fublog.FuBlog.repository.BlogPostRepository;
import tech.fublog.FuBlog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final BlogPostRepository blogPostRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       BlogPostRepository blogPostRepository) {
        this.userRepository = userRepository;
        this.blogPostRepository = blogPostRepository;
    }

    public boolean markPost(Long userId, Long postId) {
        boolean result = false;
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isPresent()) {
            Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(postId);
            if (blogPostEntity.isPresent()) {
                Set<BlogPostEntity> entitySet;
                if (userEntity.get().getMarkPosts().isEmpty()) {
                    entitySet = new HashSet<>();
                    entitySet.add(blogPostEntity.get());
                    userEntity.get().setMarkPosts(entitySet);
                    userRepository.save(userEntity.get());
                    result = true;
                } else {
                    entitySet = userEntity.get().getMarkPosts();
                    if (entitySet.add(blogPostEntity.get())) {
                        userEntity.get().setMarkPosts(entitySet);
                        userRepository.save(userEntity.get());
                        result = true;
                    } else throw new UserException("You already mark this post!");
                }
            }
        }
        return result;
    }

}
