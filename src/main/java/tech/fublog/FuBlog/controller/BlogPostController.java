package tech.fublog.FuBlog.controller;

import org.springframework.data.domain.Page;
import tech.fublog.FuBlog.dto.BlogPostDTO;
import tech.fublog.FuBlog.dto.request.RequestBlogPostDTO;
import tech.fublog.FuBlog.dto.response.PageResponse;
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
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "deleted successful", ""));
        } catch (BlogPostException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertBlogPost(@RequestBody RequestBlogPostDTO blogPostDTO) {
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
    public ResponseEntity<ResponseObject> updateBlog(@RequestBody RequestBlogPostDTO requestBlogPostDTO) {
        try {
            BlogPostEntity blogPostEntity = blogPostService.updateBlogPost(requestBlogPostDTO);
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

//    @GetMapping("/search/{category}")
//    ResponseEntity<ResponseObject> findBlogByCategory(@PathVariable String category) {
//        return blogPostService.findBlogByCategory(category);
//    }

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


    @GetMapping("getAllBlog/{page}/{size}")
    public PageResponse getAllBlog(@PathVariable int page, @PathVariable int size) {
        return blogPostService.getAllBlogPost(page, size);
    }

    @GetMapping("getByTitle/{title}/{page}/{size}")
    public PageResponse getBlogByTitle(@PathVariable String title, @PathVariable int page, @PathVariable int size) {
        return blogPostService.getAllBlogPostByTitle(title, page, size);
    }

    @GetMapping("getByCategory/{categoryId}/{page}/{size}")
    public ResponseEntity<PageResponse> getBlogPostsByCategoryId(@PathVariable Long categoryId, @PathVariable int page, @PathVariable int size) {
//        Page<BlogPostEntity> blogPosts = blogPostService.getBlogPostsByCategoryId(categoryId, page, size);
        PageResponse blogPosts = blogPostService.getBlogPostsByCategoryId(categoryId, page, size);

        if (blogPosts == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(blogPosts);
    }

    @GetMapping("/sorted/{page}/{size}")
//    public ResponseEntity<Page<BlogPostEntity>> getSortedBlogPosts(
    public ResponseEntity<PageResponse> getSortedBlogPosts(
            @RequestParam(name = "sortBy", defaultValue = "newest") String sortBy,
            @PathVariable int page, @PathVariable int size) {
//        Page<BlogPostEntity> blogPostEntities = blogPostService.getSortedBlogPosts(sortBy, page, size);
        PageResponse blogPostEntities = blogPostService.getSortedBlogPosts(sortBy, page, size);

        if (blogPostEntities == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(blogPostEntities);
    }


//    @GetMapping("/view")
//    ResponseEntity<ResponseObject> findByApproved(@RequestBody BlogPostDTO blogPostDTO) {
////        Long vote = voteService.countVote(blogPostDTO.getPostId());
////        List<ResponseCommentDTO> comment = commentService.viewComment(blogPostDTO.getPostId());
//        ResponseBlogPostDTO responseBlogPostDTO = blogPostService.viewBlogPost(blogPostDTO.getPostId(), vote, comment);
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(new ResponseObject("ok", "list here", responseBlogPostDTO));
//    }

//    @GetMapping("/search/category")
//    ResponseEntity<ResponseObject> findBlogByCategory(@RequestBody CategoryDTO categoryDTO) {
//        try {
//            Set<BlogPostDTO> dtoList = blogPostService.findBlogByCategory(categoryDTO.getCategoryName(), categoryDTO.getParentCategoryId());
//            return ResponseEntity.status(HttpStatus.OK)
//                    .body(new ResponseObject("ok", "found", dtoList));
//        } catch (BlogPostException ex) {
//            System.out.println(ex.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(new ResponseObject("failed", ex.getMessage(), ""));
//        }
//    }

//    @GetMapping("/search/title/{title}")
//    ResponseEntity<ResponseObject> findBlogByTitle(@PathVariable String title) {
//        try {
//            List<BlogPostDTO> dtoList = blogPostService.findBlogByTitle(title);
//            return ResponseEntity.status(HttpStatus.OK)
//                    .body(new ResponseObject("ok", "found", dtoList));
//        } catch (BlogPostException ex) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(new ResponseObject("failed", ex.getMessage(), ""));
//        }
//    }
}

