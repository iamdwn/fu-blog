package tech.fublog.FuBlog.controller;

import tech.fublog.FuBlog.dto.BlogPostDTO;
import tech.fublog.FuBlog.dto.request.BlogPostRequestDTO;
import tech.fublog.FuBlog.dto.response.PaginationResponseDTO;
import tech.fublog.FuBlog.entity.BlogPostEntity;
import tech.fublog.FuBlog.model.ResponseObject;
import tech.fublog.FuBlog.service.ApprovalRequestService;
import tech.fublog.FuBlog.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.fublog.FuBlog.exception.BlogPostException;
import tech.fublog.FuBlog.service.*;
import org.springframework.http.HttpStatus;

import java.util.List;


@RestController
@RequestMapping("/api/v1/auth/blogPosts")
@CrossOrigin(origins = {"http://localhost:5173", "https://fublog.tech"})
//@CrossOrigin(origins = "*")
public class BlogPostController {
    private final BlogPostService blogPostService;
    private final ApprovalRequestService approvalRequestService;
        private final VoteService voteService;
        private final CommentService commentService;
    private final PostTagService postTagService;

    @Autowired
    public BlogPostController(BlogPostService blogPostService, ApprovalRequestService approvalRequestService,
                              VoteService voteService, CommentService commentService,
                              PostTagService postTagService
    ) {
        this.blogPostService = blogPostService;
        this.approvalRequestService = approvalRequestService;
        this.voteService = voteService;
        this.commentService = commentService;
        this.postTagService = postTagService;
    }



    @DeleteMapping("/deleteBlogById/{postId}")
    public ResponseEntity<ResponseObject> deleteBlog(@PathVariable Long postId) {
        try {
            blogPostService.deleteBlogPost(postId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "deleted successful", ""));
        } catch (BlogPostException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertBlogPost(@RequestBody BlogPostRequestDTO blogPostDTO) {
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

    @PutMapping("/edit")
    public ResponseEntity<ResponseObject> updateBlog(@RequestBody BlogPostRequestDTO blogPostRequestDTO) {
        try {
            BlogPostEntity blogPostEntity = blogPostService.updateBlogPost(blogPostRequestDTO);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "updated successful", blogPostEntity));
        } catch (BlogPostException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("getPinnedBlog")
    public ResponseEntity<ResponseObject> getPinnedBlog(){
        return  blogPostService.getPinnedBlog();
    }

    @PostMapping("pinBlogAction/{postId}")
    public ResponseEntity<ResponseObject> pinBlog(@PathVariable Long postId){
        return  blogPostService.pinBlogAction(postId);
    }


    @GetMapping("/getBlogById/{postId}")
    ResponseEntity<ResponseObject> getBlogPostById(@PathVariable Long postId) {
        try {
            BlogPostDTO dto = blogPostService.getBlogPostById(postId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "post found", dto));
        } catch (BlogPostException ex) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/getBlogDetailsById/{postId}")
    ResponseEntity<ResponseObject> getBlogDetailsById(@PathVariable Long postId) {
        try {
            BlogPostEntity blogPostEntity = blogPostService.getBlogPostDetailsById(postId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "post found", blogPostEntity));
        } catch (BlogPostException ex) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/getPopularBlogPost")
    public ResponseEntity<ResponseObject> getPopularBlog() {
        try {
            List<BlogPostDTO> blogPostEntity = blogPostService.getPopularBlogPost();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "post found", blogPostEntity));
        } catch (BlogPostException ex) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("getAllBlog/{page}/{size}")
    public PaginationResponseDTO getAllBlog(@PathVariable int page, @PathVariable int size) {
        return blogPostService.getAllBlogPost(page, size);
    }

    @GetMapping("getByTitle/{title}/{page}/{size}")
    public PaginationResponseDTO getBlogByTitle(@PathVariable String title, @PathVariable int page, @PathVariable int size) {
        return blogPostService.getAllBlogPostByTitle(title, page, size);
    }

    @GetMapping("getByCategory/{categoryId}/{page}/{size}")
    public ResponseEntity<PaginationResponseDTO> getBlogPostsByCategoryId(@PathVariable Long categoryId, @PathVariable int page, @PathVariable int size) {
//        Page<BlogPostEntity> blogPosts = blogPostService.getBlogPostsByCategoryId(categoryId, page, size);
        PaginationResponseDTO blogPosts = blogPostService.getBlogPostsByCategoryId(categoryId, page, size);

        if (blogPosts == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(blogPosts);
    }



    @GetMapping("/sorted/{page}/{size}")
//    public ResponseEntity<Page<BlogPostEntity>> getSortedBlogPosts(
    public ResponseEntity<PaginationResponseDTO> getSortedBlogPosts(
            @RequestParam(name = "sortBy", defaultValue = "newest") String sortBy,
            @PathVariable int page, @PathVariable int size) {
//        Page<BlogPostEntity> blogPostEntities = blogPostService.getSortedBlogPosts(sortBy, page, size);
        PaginationResponseDTO blogPostEntities = blogPostService.getSortedBlogPosts(sortBy, page, size);

        if (blogPostEntities == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(blogPostEntities);
    }

}

