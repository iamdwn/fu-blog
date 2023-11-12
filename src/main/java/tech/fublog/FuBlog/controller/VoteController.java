package tech.fublog.FuBlog.controller;

import tech.fublog.FuBlog.Utility.TokenChecker;
import tech.fublog.FuBlog.dto.VoteDTO;
import tech.fublog.FuBlog.exception.VoteException;
import tech.fublog.FuBlog.model.ResponseObject;
import tech.fublog.FuBlog.repository.BlogPostRepository;
import tech.fublog.FuBlog.repository.UserRepository;
import tech.fublog.FuBlog.service.AwardService;
import tech.fublog.FuBlog.service.NotificationStorageService;
import tech.fublog.FuBlog.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth/blogPosts/vote")
@CrossOrigin(origins = {"http://localhost:5173", "https://fublog.tech"})

public class VoteController {
    private final VoteService voteService;
    private final AwardService awardService;
    private final NotificationStorageService notificationStorageService;
    private final UserRepository userRepository;
    private final BlogPostRepository blogPostRepository;

    @Autowired
    public VoteController(VoteService voteService, AwardService awardService, NotificationStorageService notificationStorageService, UserRepository userRepository, BlogPostRepository blogPostRepository) {
        this.voteService = voteService;
        this.awardService = awardService;
        this.notificationStorageService = notificationStorageService;
        this.userRepository = userRepository;
        this.blogPostRepository = blogPostRepository;
    }

    @GetMapping("/view/{postId}")
    public ResponseEntity<ResponseObject> viewVote(@PathVariable Long postId) {
        try {
            List<VoteDTO> dtoList = voteService.viewVote(postId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "found", dtoList));
        } catch (VoteException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/count/{postId}")
    public ResponseEntity<ResponseObject> countVote(@PathVariable Long postId) {
        try {
            Long count = voteService.countVote(postId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "found", count));
        } catch (VoteException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @PostMapping("/insert")
    public ResponseEntity<ResponseObject> insertVote(@RequestHeader("Authorization") String token,
                                                     @RequestBody VoteDTO voteDTO) {
        try {
            if (TokenChecker.checkToken(token)) {
                VoteDTO dto = voteService.insertVote(voteDTO);
                if (dto != null) {
//                Optional<UserEntity> userEntity = userRepository.findById(voteDTO.getUserId());
//                Optional<BlogPostEntity> blogPostEntity = blogPostRepository.findById(voteDTO.getPostId());
//                NotificationEntity notificationEntity = new NotificationEntity();
//                notificationEntity.setDelivered(false);
//                notificationEntity.setContent(userEntity.get().getFullName() + "was voted your post");
//                notificationEntity.setUserNotiId(blogPostEntity.get().getAuthors());
//                notificationStorageService.createNotificationStorage(notificationEntity);
                    awardService.checkAward(voteDTO.getUserId());
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseObject("ok", "Vote have been inserted", dto));
                } else
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseObject("ok", "Vote have been deleted", ""));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

}
