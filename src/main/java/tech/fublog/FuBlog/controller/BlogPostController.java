package tech.fublog.FuBlog.controller;

import org.springframework.data.domain.Page;
import tech.fublog.FuBlog.dto.BlogPostDTO;
import tech.fublog.FuBlog.dto.SortDTO;
import tech.fublog.FuBlog.entity.BlogPostEntity;
import tech.fublog.FuBlog.model.ResponseObject;
import tech.fublog.FuBlog.service.ApprovalRequestService;
import tech.fublog.FuBlog.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.fublog.FuBlog.dto.CategoryDTO;
import tech.fublog.FuBlog.dto.response.ResponseBlogPostDTO;
import tech.fublog.FuBlog.dto.response.ResponseCommentDTO;
import tech.fublog.FuBlog.exception.BlogPostException;
import tech.fublog.FuBlog.service.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/api/v1/auth/blogPosts")
//    @CrossOrigin(origins = {"http://localhost:5173", "https://fublog.tech"})
@CrossOrigin(origins = "*")
public class BlogPostController {
    private final BlogPostService blogPostService;
    private final ApprovalRequestService approvalRequestService;
    private final VoteService voteService;
    private final CommentService commentService;
    private final PostTagService postTagService;

//    @Autowired
//    private BlogPostService blogPostService;
//
//    @Autowired
//    private ApprovalRequestService approvalRequestService;

    @Autowired
    public BlogPostController(BlogPostService blogPostService, ApprovalRequestService approvalRequestService, VoteService voteService, CommentService commentService, PostTagService postTagService) {
        this.blogPostService = blogPostService;
        this.approvalRequestService = approvalRequestService;
        this.voteService = voteService;
        this.commentService = commentService;
        this.postTagService = postTagService;
    }

//    @GetMapping("/viewAllBlogs")
////    @PreAuthorize("hasAuthority('USER')")
////    @PreAuthorize("hasRole('USER')")
////    @PreAuthorize("isAuthenticated()")
//    ResponseEntity<ResponseObject> getAllBlogPost() {
//
//        return blogPostService.getAllBlogPosts();
//    }


    @DeleteMapping("/deleteBlog/{postId}")
    public ResponseEntity<ResponseObject> deleteBlog(@PathVariable Long postId) {

        return blogPostService.deleteBlogPost(postId);
    }


    //    @PostMapping("/writeBlog")
//    @PreAuthorize("isAuthenticated()")
//    @PreAuthorize("hasRole('USER')")
//    @PreAuthorize("hasAuthority('WRITE_BLOG')")
//    ResponseEntity<ResponseObject> insertBlogPost(
//            @RequestBody BlogPostDTO blogPostDTO) {
////            String user = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        BlogPostEntity blogPostEntity = blogPostService.createBlogPost(blogPostDTO);
//        if (blogPostEntity != null) {
//            return approvalRequestService.createApprovalRequestById(blogPostEntity);
//        }
//
//
//        return null;
//    }
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

    @PutMapping("/editBlog/{postId}")
    public ResponseEntity<ResponseObject> updateBlog(
            @PathVariable Long postId,
            @RequestBody BlogPostDTO blogPostDTO
    ) {

        return blogPostService.updateBlogPost(postId, blogPostDTO);
    }


//    @GetMapping("/search/{category}")
//    ResponseEntity<ResponseObject> findBlogByCategory(@PathVariable String category) {
//        return blogPostService.findBlogByCategory(category);
//    }

    @GetMapping("/viewBlog/{postId}")
    ResponseEntity<ResponseObject> getBlogPostById(@PathVariable Long postId) {

        return blogPostService.getBlogPostById(postId);
    }


    @GetMapping("getAllBlog/{page}/{size}")
    public List<BlogPostEntity> getAllBlog(@PathVariable int page, @PathVariable int size) {
        return blogPostService.getAllBlogPost(page, size);
    }

    @GetMapping("getByTitle/{title}/{page}/{size}")
    public List<BlogPostEntity> getBlogByTitle(@PathVariable String title, @PathVariable int page, @PathVariable int size) {
        return blogPostService.getAllBlogPostByTitle(title, page, size);
    }

    @GetMapping("/byCategory/{categoryId}/{page}/{size}")
    public ResponseEntity<Page<BlogPostEntity>> getBlogPostsByCategoryId(@PathVariable Long categoryId, @PathVariable int page, @PathVariable int size) {
        Page<BlogPostEntity> blogPosts = blogPostService.getBlogPostsByCategoryId(categoryId, page, size);

        if (blogPosts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(blogPosts);
    }

    @GetMapping("/sorted/{page}/{size}")
    public ResponseEntity<Page<BlogPostEntity>> getSortedBlogPosts(
            @RequestParam(name = "sortBy", defaultValue = "newest") String sortBy,
            @PathVariable int page, @PathVariable int size) {
        Page<BlogPostEntity> blogPostEntities = blogPostService.getSortedBlogPosts(sortBy, page, size);

        if (blogPostEntities.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(blogPostEntities);
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
}

