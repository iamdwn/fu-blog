package tech.fublog.FuBlog.controller;

import tech.fublog.FuBlog.Utility.TokenChecker;
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
    private final JwtService jwtService;
    private final PostTagService postTagService;
    private final AwardService awardService;

    @Autowired
    public BlogPostController(BlogPostService blogPostService, ApprovalRequestService approvalRequestService,
                              VoteService voteService, CommentService commentService,
                              JwtService jwtService, PostTagService postTagService,
                              AwardService awardService) {
        this.blogPostService = blogPostService;
        this.approvalRequestService = approvalRequestService;
        this.voteService = voteService;
        this.commentService = commentService;
        this.jwtService = jwtService;
        this.postTagService = postTagService;
        this.awardService = awardService;
    }


    @DeleteMapping("/deleteBlogById/{postId}")
    public ResponseEntity<ResponseObject> deleteBlog(@RequestHeader("Authorization") String token,
                                                     @PathVariable Long postId) {
        try {
            if (TokenChecker.checkToken(token)) {
                blogPostService.deleteBlogPost(postId);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "deleted successful", ""));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertBlogPost(@RequestHeader("Authorization") String token,
                                                  @RequestBody BlogPostRequestDTO blogPostDTO) {
        try {
            if (TokenChecker.checkToken(token)) {
                BlogPostEntity blogPostEntity = blogPostService.insertBlogPost(blogPostDTO);
                approvalRequestService.insertApprovalRequest(blogPostEntity);
                postTagService.insertPostTag(blogPostDTO.getTagList(), blogPostEntity);
                awardService.checkAward(blogPostDTO.getUserId());
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "post is waiting approve", ""));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<ResponseObject> updateBlog(@RequestHeader("Authorization") String token,
                                                     @RequestBody BlogPostRequestDTO blogPostRequestDTO) {
        try {
            if (TokenChecker.checkToken(token)) {
                BlogPostEntity blogPostEntity = blogPostService.updateBlogPost(blogPostRequestDTO);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "updated successful", blogPostEntity));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/getPinnedBlog")
    public ResponseEntity<ResponseObject> getPinnedBlog(@RequestHeader("Authorization") String token) {
        try {
            if (TokenChecker.checkRole(token, false)) {
                return blogPostService.getPinnedBlog();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @PostMapping("/pinBlogAction/{postId}")
    public ResponseEntity<ResponseObject> pinBlog(@RequestHeader("Authorization") String token,
                                                  @PathVariable Long postId) {
        try {
            if (TokenChecker.checkRole(token, false)) {
                return blogPostService.pinBlogAction(postId);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }


    @GetMapping("/getBlogById/{postId}")
    ResponseEntity<ResponseObject> getBlogPostById(@PathVariable Long postId) {
        try {
            BlogPostDTO dto = blogPostService.getBlogPostById(postId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "post found", dto));
        } catch (BlogPostException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/getBlogPostByAuthor/{userId}/{page}/{size}")
    ResponseEntity<ResponseObject> getBlogPostByAuthor(@PathVariable Long userId,
                                                       @PathVariable int page, @PathVariable int size) {
        try {
            PaginationResponseDTO dtoList = blogPostService.getBlogPostByAuthor(userId, page, size);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "post found", dtoList));
        } catch (BlogPostException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

//    @GetMapping("/getBlogPostByTag/{tagId}")
//    ResponseEntity<ResponseObject> getBlogPostByTag(@PathVariable Long tagId) {
//        try {
//            List<BlogPostDTO> dtoList = blogPostService.getBlogPostByTag(tagId);
//            return ResponseEntity.status(HttpStatus.OK)
//                    .body(new ResponseObject("ok", "post found", dtoList));
//        } catch (BlogPostException ex) {
//            return ResponseEntity.status(HttpStatus.OK)
//                    .body(new ResponseObject("failed", ex.getMessage(), ""));
//        }
//    }

    @GetMapping("/getBlogDetailsById/{postId}")
    ResponseEntity<ResponseObject> getBlogDetailsById(@PathVariable Long postId) {
        try {
            BlogPostEntity blogPostEntity = blogPostService.getBlogPostDetailsById(postId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "post found", blogPostEntity));
        } catch (BlogPostException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/getPopularBlogPostByView")
    public ResponseEntity<ResponseObject> getPopularBlogByView() {
        try {
            List<BlogPostDTO> blogPostEntity = blogPostService.getPopularBlogPostByView();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "post found", blogPostEntity));
        } catch (BlogPostException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/getPopularBlogPostByVote")
    public ResponseEntity<ResponseObject> getPopularBlogByVote() {
        try {
            List<BlogPostDTO> blogPostEntity = blogPostService.getPopularBlogPostByVote();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "post found", blogPostEntity));
        } catch (BlogPostException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/getAllBlog/{page}/{size}")
    public ResponseEntity<ResponseObject> getAllBlog(@PathVariable int page, @PathVariable int size) {

        try {
            PaginationResponseDTO blogPosts = blogPostService.getAllBlogPost(page, size);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "post found", blogPosts));
        } catch (BlogPostException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }


    @GetMapping("/getAllBlog")
    public ResponseEntity<ResponseObject> getAllBlogs() {

        try {
            PaginationResponseDTO blogPosts = blogPostService.getAllBlogPost();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "post found", blogPosts));
        } catch (BlogPostException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/getByTitle/{title}/{page}/{size}")
    public ResponseEntity<ResponseObject> getBlogByTitle(@PathVariable String title, @PathVariable int page, @PathVariable int size) {
        try {
            PaginationResponseDTO blogPosts = blogPostService.getAllBlogPostByTitle(title, page, size);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "post found", blogPosts));
        } catch (BlogPostException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/getByCategory/{categoryId}/{page}/{size}")
    public ResponseEntity<ResponseObject> getBlogPostsByCategoryId(@PathVariable Long categoryId, @PathVariable int page, @PathVariable int size) {
//        Page<BlogPostEntity> blogPosts = blogPostService.getBlogPostsByCategoryId(categoryId, page, size);
        try {
            PaginationResponseDTO blogPosts = blogPostService.getBlogPostsByCategoryId(categoryId, page, size);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "post found", blogPosts));
        } catch (BlogPostException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/getByCategory/{categoryId}")
    public ResponseEntity<ResponseObject> getBlogPostsByCategoryId(@PathVariable Long categoryId) {
//        Page<BlogPostEntity> blogPosts = blogPostService.getBlogPostsByCategoryId(categoryId, page, size);
        try {
            PaginationResponseDTO blogPosts = blogPostService.getBlogPostsByCategoryId(categoryId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "post found", blogPosts));
        } catch (BlogPostException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/getBlogByFollow/{userId}")
    public ResponseEntity<ResponseObject> getBlogByFollow(@PathVariable Long userId) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "post found",  blogPostService.getBlogByFollow(userId)));
        } catch (BlogPostException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/sorted/{page}/{size}")
    public ResponseEntity<ResponseObject> getSortedBlogPosts(
            @RequestParam(name = "sortBy", defaultValue = "newest") String sortBy,
            @PathVariable int page, @PathVariable int size) {
        try {
            PaginationResponseDTO blogPosts = blogPostService.getSortedBlogPosts(sortBy, page, size);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "post found", blogPosts));
        } catch (BlogPostException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/bytag/{page}/{size}")
    public ResponseEntity<ResponseObject> getBlogPostsByTag(@RequestParam(name = "tag") String tagName,
                                                            @PathVariable int page, @PathVariable int size) {
        try {
            PaginationResponseDTO blogPosts = blogPostService.findByTagName(tagName, page, size);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "post found", blogPosts));
        } catch (BlogPostException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/getBlogByTrending/{page}/{size}")
    public ResponseEntity<ResponseObject> getBlogByTrending(@PathVariable int page,
                                                            @PathVariable int size) {
        try {
            PaginationResponseDTO blogPosts = blogPostService.getBlogByTrending(page, size);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "post found", blogPosts));
        } catch (BlogPostException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/getBlogByVote/{page}/{size}")
    public ResponseEntity<ResponseObject> getBlogByVote(@PathVariable int page,
                                                        @PathVariable int size) {
        try {
            PaginationResponseDTO blogPosts = blogPostService.getBlogByVote(page, size);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "post found", blogPosts));
        } catch (BlogPostException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }
}

