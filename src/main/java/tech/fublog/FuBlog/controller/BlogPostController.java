package tech.fublog.FuBlog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.fublog.FuBlog.entity.BlogPostEntity;
import tech.fublog.FuBlog.service.BlogPostService;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class BlogPostController {

    @Autowired
    BlogPostService blogPostService;

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
    public ResponseEntity<Page<BlogPostEntity>> getSortedBlogPosts(
            @RequestParam(name = "sortBy", defaultValue = "newest") String sortBy,
            @PathVariable int page, @PathVariable int size) {
        Page<BlogPostEntity> sortedBlogPosts = blogPostService.getSortedBlogPosts(sortBy, page, size);

        if (sortedBlogPosts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(sortedBlogPosts);
    }
}
