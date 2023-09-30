package tech.fublog.FuBlog.controller;

import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import tech.fublog.FuBlog.dto.BlogPostDTO;
import tech.fublog.FuBlog.dto.SortDTO;
import tech.fublog.FuBlog.entity.BlogPostEntity;
import tech.fublog.FuBlog.model.ResponseObject;
import tech.fublog.FuBlog.service.ApprovalRequestService;
import tech.fublog.FuBlog.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/auth/blogPosts")
//    @CrossOrigin(origins = {"http://localhost:5173", "https://fublog.tech"})
@CrossOrigin(origins = "*")
public class  BlogPostController {

    @Autowired
    private BlogPostService blogPostService;

    @Autowired
    private ApprovalRequestService approvalRequestService;


    @GetMapping("/viewAllBlogs")
//    @PreAuthorize("hasAuthority('USER')")
//    @PreAuthorize("hasRole('USER')")
//    @PreAuthorize("isAuthenticated()")
    ResponseEntity<ResponseObject> getAllBlogPost() {

        return blogPostService.getAllBlogPosts();
    }


    @DeleteMapping("/deleteBlog/{postId}")
    public ResponseEntity<ResponseObject> deleteBlog(@PathVariable Long postId) {

        return blogPostService.deleteBlogPost(postId);
    }


    @PostMapping("/writeBlog")
//    @PreAuthorize("isAuthenticated()")
//    @PreAuthorize("hasRole('USER')")
//    @PreAuthorize("hasAuthority('WRITE_BLOG')")
    ResponseEntity<ResponseObject> insertBlogPost(
            @RequestBody BlogPostDTO blogPostDTO) {
//            String user = SecurityContextHolder.getContext().getAuthentication().getName();

        BlogPostEntity blogPostEntity = blogPostService.createBlogPost(blogPostDTO);
        if (blogPostEntity != null) {
            return approvalRequestService.createApprovalRequestById(blogPostEntity);
        }


        return null;
    }


    @PutMapping("/editBlog/{postId}")
    public ResponseEntity<ResponseObject> updateBlog(
            @PathVariable Long postId,
            @RequestBody BlogPostDTO blogPostDTO
    ) {

        return blogPostService.updateBlogPost(postId, blogPostDTO);
    }


    @GetMapping("/search/{category}")
    ResponseEntity<ResponseObject> findBlogByCategory(@PathVariable String category) {
        return blogPostService.findBlogByCategory(category);
    }

    @GetMapping("/viewBlog/{postId}")
    ResponseEntity<ResponseObject> getBlogPostById(@PathVariable Long postId) {

        return blogPostService.getBlogPostById(postId);
    }

    @GetMapping("getPinnedBlog")
    public ResponseEntity<ResponseObject> getPinnedBlog(){
        return  blogPostService.getPinnedBlog();
    }

    @PostMapping("pinBlogAction/{postId}")
    public ResponseEntity<ResponseObject> pinBlog(@PathVariable Long postId){
        return  blogPostService.pinBlogAction(postId);
    }


    @GetMapping("getAllBlog/{page}/{size}")
    public List<BlogPostEntity> getAllBlog(@PathVariable int page, @PathVariable int size){
        return  blogPostService.getAllBlogPost(page, size);
    }

    @GetMapping("getByTitle/{title}/{page}/{size}")
    public List<BlogPostEntity> getBlogByTitle(@PathVariable String title,@PathVariable int page, @PathVariable int size){
        return  blogPostService.getAllBlogPostByTitle(title,page,size);
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
    public ResponseEntity<Page<SortDTO>> getSortedBlogPosts(
            @RequestParam(name = "sortBy", defaultValue = "newest") String sortBy,
            @PathVariable int page, @PathVariable int size) {
        Page<SortDTO> sortedBlogPosts = blogPostService.getSortedBlogPosts(sortBy, page, size);

        if (sortedBlogPosts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(sortedBlogPosts);
    }

}
