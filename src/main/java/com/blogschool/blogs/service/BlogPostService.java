package com.blogschool.blogs.service;

import com.blogschool.blogs.entity.BlogPostEntity;
import com.blogschool.blogs.entity.CategoryEntity;
import com.blogschool.blogs.entity.UserEntity;
import com.blogschool.blogs.repository.ApprovalRequestRepository;
import com.blogschool.blogs.repository.BlogPostRepository;
import com.blogschool.blogs.repository.CategoryRepository;
import com.blogschool.blogs.repository.UserRepository;
import com.blogschool.blogs.model.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private ApprovalRequestService approvalRequestService;


    public ResponseEntity<ResponseObject> getAllBlogPosts() {
        List<BlogPostEntity> blogPostEntity = blogPostRepository.findAll();
        List<BlogPostEntity> blogPostList = new ArrayList<>();

        if (!blogPostEntity.isEmpty()) {
            for (int i = 0; i < blogPostEntity.size(); i++) {
                if (blogPostEntity.get(i).getStatus()) {
                    blogPostList.add(blogPostEntity.get(i));
                }
            }
            sort(blogPostList);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "FOUND", blogPostList));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseObject("failed", "NOT FOUND", ""));
    }


    //lấy cả BlogPost bị xoá
//    public List<BlogPostEntity> getAllBlogPosts2() {
//        return blogPostRepository.findAll();
//    }


    public BlogPostEntity getBlogPostById(Long postId) {

        return blogPostRepository.findById(postId).orElse(null);
    }


    //xoá --> set Status = 0
    public ResponseEntity<ResponseObject> deleteBlogPost(Long postId) {
        Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(postId);

        if (blogPostEntity.isPresent()) {
            BlogPostEntity blogPost = this.getBlogPostById(postId);

            blogPost.setStatus(false);

            blogPostRepository.save(blogPost);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "DELETED SUCCESSFUL", blogPost));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseObject("failed", "DELETED FAILED", ""));
    }


    //xoá luôn dữ liệu trong Database
//    public void deleteBlogPost2(Long postId) {
//        BlogPostEntity blogPostEntity = this.getBlogPostById(postId);
//        blogPostRepository.delete(blogPostEntity);
//    }


    public ResponseEntity<ResponseObject> createBlogPost(String typePost, String title, String content,
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

            if (newBlogPostEntity.isPresent()) {
                approvalRequestService.createApprovalRequestById(newBlogPostEntity.get(), authors);

                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "CREATED SUCCESSFUL", newBlogPost));
            } else {

                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseObject("failed", "CREATED FAILED", ""));
            }
        }

        return null;
    }


    public ResponseEntity<ResponseObject> updateBlogPost(Long postId, String typePost, String title, String content,
                                                         Long categoryId, Long authorsModified) {

        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(categoryId);
        Optional<UserEntity> userEntity = userRepository.findById(authorsModified);
        Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(postId);

        if (blogPostEntity.isPresent()
                && categoryEntity.isPresent()
                && userEntity.isPresent()) {

            BlogPostEntity blogPost = this.getBlogPostById(postId);

            CategoryEntity category = categoryEntity.get();
            UserEntity authors = userEntity.get();
            Date modifiedDate = new Date();

            blogPost.setTypePost(typePost);
            blogPost.setTitle(title);
            blogPost.setContent(content);
            blogPost.setModifiedDate(modifiedDate);
            blogPost.setCategory(category);
            blogPost.setAuthorsModified(authors);

            blogPostRepository.save(blogPost);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "UPDATED SUCCESSFUL", blogPost));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseObject("failed", "UPDATED FAILED", ""));
    }


    public ResponseEntity<ResponseObject> findBlogByCategory(String name) {

        CategoryEntity category = categoryRepository.findByCategoryName(name);

        if (category != null) {

            List<BlogPostEntity> blogPostList = category.getBlogPosts();

            if (!blogPostList.isEmpty()) {

                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "FOUND", blogPostList));
            } else {

                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseObject("failed", "cannot found any blog with category", ""));
            }
        } else {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("failed", "category not exists", ""));
        }
    }


    //sort theo thứ tự bài BlogPost mới nằm đầu tiên (giảm dần theo postId)
    public void sort(List<BlogPostEntity> blogPostList) {

        Collections.sort(blogPostList, new Comparator<BlogPostEntity>() {

            @Override
            public int compare(BlogPostEntity o1, BlogPostEntity o2) {

                if (o1.getPostId() < o2.getPostId()) {
                    return 1;
                } else if (o1.getPostId() == o2.getPostId()) {
                    return 0;
                } else {
                    return -1;
                }
            }

        });
    }

}
