package tech.fublog.FuBlog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tech.fublog.FuBlog.entity.BlogPostEntity;
import tech.fublog.FuBlog.entity.CategoryEntity;
import tech.fublog.FuBlog.repository.BlogPostRepository;
import tech.fublog.FuBlog.repository.CategoryRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class BlogPostService {
    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<BlogPostEntity> getAllBlogPost(int page, int size){
        Pageable pageable = PageRequest.of(page-1,size);
        return  blogPostRepository.findAll(pageable).get().toList();
    }

    public List<BlogPostEntity> getAllBlogPostByTitle(String title,int page, int size){
        Pageable pageable = PageRequest.of(page-1,size);
        return  blogPostRepository.getBlogPostEntitiesByTitle(title, pageable);
    }

    public Page<BlogPostEntity> getBlogPostsByCategoryId(Long categoryId,int page, int size) {
        Pageable pageable = PageRequest.of(page-1,size);
        Optional<CategoryEntity> categoryOptional = categoryRepository.findById(categoryId);

        if (!categoryOptional.isPresent()) {
            return new PageImpl<>(Collections.emptyList());
        }

        CategoryEntity category = categoryOptional.get();
        return blogPostRepository.findByCategory(category,pageable);
    }

    public Page<BlogPostEntity> getSortedBlogPosts(String sortBy, int page, int size) {
        Pageable pageable = PageRequest.of(page-1,size);
        Page<BlogPostEntity> sortedBlogPosts;

        if ("newest".equalsIgnoreCase(sortBy)) {
            sortedBlogPosts = blogPostRepository.findAllByOrderByCreatedDateDesc(pageable);
        } else if ("oldest".equalsIgnoreCase(sortBy)) {
            sortedBlogPosts = blogPostRepository.findAllByOrderByCreatedDateAsc(pageable);
        } else if ("latestModified".equalsIgnoreCase(sortBy)) {
            sortedBlogPosts = blogPostRepository.findAllByOrderByModifiedDateDesc(pageable);
        } else if ("oldestModified".equalsIgnoreCase(sortBy)) {
            sortedBlogPosts = blogPostRepository.findAllByOrderByModifiedDateAsc(pageable);
        } else {
            // Mặc định, sắp xếp theo ngày tạo mới nhất.
            sortedBlogPosts = blogPostRepository.findAllByOrderByCreatedDateDesc(pageable);
        }

        return sortedBlogPosts;
    }


}
