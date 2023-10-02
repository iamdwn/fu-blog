package com.blogschool.blogs.controller;

import com.blogschool.blogs.dto.BlogPostDTO;
import com.blogschool.blogs.dto.CategoryDTO;
import com.blogschool.blogs.dto.ResponseBlogPostDTO;
import com.blogschool.blogs.dto.ResponseCommentDTO;
import com.blogschool.blogs.entity.BlogPostEntity;
import com.blogschool.blogs.entity.ResponseObject;
import com.blogschool.blogs.exception.BlogPostException;
import com.blogschool.blogs.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/blogPosts/blog")
public class BlogPostController {
    private final BlogPostService blogPostService;
    private final ApprovalRequestService approvalRequestService;
    private final VoteService voteService;
    private final CommentService commentService;
    private final PostTagService postTagService;

    @Autowired
    public BlogPostController(BlogPostService blogPostService, ApprovalRequestService approvalRequestService, VoteService voteService, CommentService commentService, PostTagService postTagService) {
        this.blogPostService = blogPostService;
        this.approvalRequestService = approvalRequestService;
        this.voteService = voteService;
        this.commentService = commentService;
        this.postTagService = postTagService;
    }

    @GetMapping("/view")
    ResponseEntity<ResponseObject> findByApproved(@RequestBody BlogPostDTO blogPostDTO) {
        Long vote = voteService.countVote(blogPostDTO.getPostId());
        List<ResponseCommentDTO> comment = commentService.viewComment(blogPostDTO.getPostId());
        ResponseBlogPostDTO responseBlogPostDTO = blogPostService.viewBlogPost(blogPostDTO.getPostId(), vote, comment);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("ok", "list here", responseBlogPostDTO));
    }

    @GetMapping("/search/category")
    ResponseEntity<ResponseObject> findBlogByCategory(@RequestBody CategoryDTO categoryDTO) {
        try {
            Set<BlogPostDTO> dtoList = blogPostService.findBlogByCategory(categoryDTO.getCategoryName(), categoryDTO.getParentCategoryId());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "found", dtoList));
        } catch (BlogPostException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/search/title/{title}")
    ResponseEntity<ResponseObject> findBlogByTitle(@PathVariable String title) {
        try {
            List<BlogPostDTO> dtoList = blogPostService.findBlogByTitle(title);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "found", dtoList));
        } catch (BlogPostException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertBlogPost(@RequestBody BlogPostDTO blogPostDTO) {
        try {
            BlogPostEntity blogPostEntity = blogPostService.insertBlogPost(blogPostDTO);
            approvalRequestService.insertApprovalRequest(blogPostEntity);
            postTagService.insertPostTag(blogPostDTO.getTagList(), blogPostEntity);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "post is waiting approve", ""));
        } catch (BlogPostException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

}
