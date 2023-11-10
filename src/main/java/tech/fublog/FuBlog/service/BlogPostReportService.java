package tech.fublog.FuBlog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.fublog.FuBlog.Utility.DTOConverter;
import tech.fublog.FuBlog.dto.request.BlogPostReportDTO;
import tech.fublog.FuBlog.entity.BlogPostEntity;
import tech.fublog.FuBlog.entity.BlogPostReportEntity;
import tech.fublog.FuBlog.entity.UserEntity;
import tech.fublog.FuBlog.exception.BlogPostReportException;
import tech.fublog.FuBlog.repository.BlogPostReportRepository;
import tech.fublog.FuBlog.repository.BlogPostRepository;
import tech.fublog.FuBlog.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BlogPostReportService {
    private final BlogPostReportRepository blogPostReportRepository;
    private final UserRepository userRepository;
    private final BlogPostRepository blogPostRepository;

    @Autowired
    public BlogPostReportService(BlogPostReportRepository blogPostReportRepository,
                                 UserRepository userRepository,
                                 BlogPostRepository blogPostRepository) {
        this.blogPostReportRepository = blogPostReportRepository;
        this.userRepository = userRepository;
        this.blogPostRepository = blogPostRepository;
    }

    public void createReportBlog(BlogPostReportDTO blogPostReportDTO) {
        Optional<UserEntity> user = userRepository.findById(blogPostReportDTO.getUserId());
        Optional<BlogPostEntity> post = blogPostRepository.findById(blogPostReportDTO.getBlogId());
        if (user.isPresent()
                && post.isPresent()) {
            BlogPostReportEntity blogPostReportEntity = blogPostReportRepository.findByUserAndBlog(user.get(), post.get());
            if (blogPostReportEntity == null) {
                blogPostReportEntity = new BlogPostReportEntity(blogPostReportDTO.getReason(), user.get(), post.get());
                blogPostReportRepository.save(blogPostReportEntity);
            } else throw new BlogPostReportException("You already report this post!");
        } else throw new BlogPostReportException("User or Blog doesn't valid!");
    }

    public List<BlogPostReportDTO> viewAllReportBlog() {
        List<BlogPostReportEntity> entityList = blogPostReportRepository.findByOrderByCreatedDateDesc();
        List<BlogPostReportDTO> dtoList = new ArrayList<>();
        if (!entityList.isEmpty()) {
            for (BlogPostReportEntity entity : entityList) {
                dtoList.add(DTOConverter.convertBlogReportDTO(entity));
            }
        }
        return dtoList;
    }

    public boolean checkReport(Long userId, Long blogId) {
        boolean result = false;
        Optional<UserEntity> user = userRepository.findById(userId);
        Optional<BlogPostEntity> post = blogPostRepository.findById(blogId);
        if (user.isPresent()
                && post.isPresent()) {
            BlogPostReportEntity blogPostReportEntity = blogPostReportRepository.findByUserAndBlog(user.get(), post.get());
            if (blogPostReportEntity != null) {
                result = true;
            }
            return result;
        } else throw new BlogPostReportException("User or Blog doesn't valid!");
    }
}
