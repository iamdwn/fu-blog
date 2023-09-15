package com.blogschool.blogs.service;

import com.blogschool.blogs.entity.ApprovalRequestEntity;
import com.blogschool.blogs.entity.BlogPostEntity;
import com.blogschool.blogs.entity.CategoryEntity;
import com.blogschool.blogs.entity.UserEntity;
import com.blogschool.blogs.repository.ApprovalRequestRepository;
import com.blogschool.blogs.repository.BlogPostRepository;
import com.blogschool.blogs.repository.CategoryRepository;
import com.blogschool.blogs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BlogPostService {

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApprovalRequestRepository approvalRequestRepository;

    @Autowired
    private ApprovalRequestService approvalRequestService;


    public List<BlogPostEntity> getAllBlogPosts() {
        List<BlogPostEntity> blogPostEntity = blogPostRepository.findAll();
        List<BlogPostEntity> blogPostList = new ArrayList<>();

        for (int i = 0; i < blogPostEntity.size(); i++) {
            if (blogPostEntity.get(i).getStatus()) {
                blogPostList.add(blogPostEntity.get(i));
            }
        }
        sort(blogPostList);

        return blogPostList;
    }


    //lấy cả BlogPost bị xoá
//    public List<BlogPostEntity> getAllBlogPosts() {
//        return blogPostRepository.findAll();
//    }


    public BlogPostEntity getBlogPostById(Long postId) {

        return blogPostRepository.findById(postId).orElse(null);
    }


    //xoá --> set Status = 0
    public BlogPostEntity deleteBlogPost(Long postId) {
        BlogPostEntity blogPostEntity = this.getBlogPostById(postId);

        blogPostEntity.setStatus(false);

        return blogPostRepository.save(blogPostEntity);

    }


    //xoá luôn dữ liệu trong Database
    public void deleteBlogPost2(Long postId) {                          //xoa trong database
        BlogPostEntity blogPostEntity = this.getBlogPostById(postId);
        blogPostRepository.delete(blogPostEntity);
    }


    public BlogPostEntity createBlogPost(String typePost, String title, String content,
                                         Long categoryId, Long authorsId) {
        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(categoryId);
        Optional<UserEntity> userEntity = userRepository.findById(authorsId);

        if (categoryEntity.isPresent()
                && userEntity.isPresent()) {

            CategoryEntity category = categoryEntity.get();
            UserEntity authors = userEntity.get();
            Date createdDate = new Date();


            //tạo và save bài BlogPost mới
            BlogPostEntity newBlogPost = new BlogPostEntity(typePost, title, content, createdDate, null,
                    null, false, category, authors, null, true);
            blogPostRepository.save(newBlogPost);


            //tạo Approval Request cho bài BlogPost
            Optional<BlogPostEntity> newBlogPostEntity = blogPostRepository.findById(newBlogPost.getPostId());
            approvalRequestService.createApprovalRequestById(newBlogPostEntity.get(), authors);

            return newBlogPost;
        }
        return null;
    }


    public BlogPostEntity updateBlogPost(Long postId, String typePost, String title, String content,
                                         Long categoryId, Long authorsModified) {

        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(categoryId);
        Optional<UserEntity> userEntity = userRepository.findById(authorsModified);
        BlogPostEntity blogPostEntity = this.getBlogPostById(postId);

        if (categoryEntity.isPresent()
                && userEntity.isPresent()) {

            CategoryEntity category = categoryEntity.get();
            UserEntity authors = userEntity.get();
            Date modifiedDate = new Date();

            blogPostEntity.setTypePost(typePost);
            blogPostEntity.setTitle(title);
            blogPostEntity.setContent(content);
            blogPostEntity.setModifiedDate(modifiedDate);
            blogPostEntity.setCategory(category);
            blogPostEntity.setAuthorsModified(authors);

            return blogPostRepository.save(blogPostEntity);
        }
        return null;
    }


    //sort theo thứ tự bài BlogPost mới nằm đầu tiên (giảm dần theo postId)
    public void sort(List<BlogPostEntity> blogPostList) {
        Collections.sort(blogPostList, new Comparator<BlogPostEntity>() {
            @Override
            public int compare(BlogPostEntity o1, BlogPostEntity o2) {
                if (o1.getPostId() < o2.getPostId()) {
                    return 1;
                } else {
                    if (o1.getPostId() == o2.getPostId()) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            }
        });
    }

}
