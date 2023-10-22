package tech.fublog.FuBlog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.fublog.FuBlog.Utility.DTOConverter;
import tech.fublog.FuBlog.dto.request.BlogReportDTO;
import tech.fublog.FuBlog.entity.BlogPostEntity;
import tech.fublog.FuBlog.entity.BlogPostReportEntity;
import tech.fublog.FuBlog.entity.UserEntity;
import tech.fublog.FuBlog.exception.BlogReportException;
import tech.fublog.FuBlog.repository.BlogPostReportRepository;
import tech.fublog.FuBlog.repository.BlogPostRepository;
import tech.fublog.FuBlog.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BlogReportService {
    private final BlogPostReportRepository blogPostReportRepository;
    private final UserRepository userRepository;
    private final BlogPostRepository blogPostRepository;

    @Autowired
    public BlogReportService(BlogPostReportRepository blogPostReportRepository,
                             UserRepository userRepository,
                             BlogPostRepository blogPostRepository) {
        this.blogPostReportRepository = blogPostReportRepository;
        this.userRepository = userRepository;
        this.blogPostRepository = blogPostRepository;
    }

    public void createReportBlog(BlogReportDTO blogReportDTO) {
        Optional<UserEntity> user = userRepository.findById(blogReportDTO.getUserId());
        Optional<BlogPostEntity> post = blogPostRepository.findById(blogReportDTO.getBlogId());
        if (user.isPresent()
                && post.isPresent()) {
            BlogPostReportEntity blogPostReportEntity = blogPostReportRepository.findByUserAndBlog(user.get(), post.get());
            if (blogPostReportEntity == null) {
                blogPostReportEntity = new BlogPostReportEntity(blogReportDTO.getReason(), user.get(), post.get());
                blogPostReportRepository.save(blogPostReportEntity);
            } else throw new BlogReportException("You already report this post!");
        } else throw new BlogReportException("User or Blog doesn't valid!");
    }

    public List<BlogReportDTO> viewAllReportBlog() {
        List<BlogPostReportEntity> entityList = blogPostReportRepository.findByOrderByCreatedDateDesc();
        List<BlogReportDTO> dtoList = new ArrayList<>();
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
        } else throw new BlogReportException("User or Blog doesn't valid!");
    }
}
