package tech.fublog.FuBlog.controller;

import tech.fublog.FuBlog.dto.VoteDTO;
import tech.fublog.FuBlog.entity.ResponseObject;
import tech.fublog.FuBlog.exception.VoteException;
import tech.fublog.FuBlog.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth/blogPosts/vote")
public class VoteController {
    private final VoteService voteService;

    @Autowired
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @GetMapping("/view/{postId}")
    public ResponseEntity<ResponseObject> viewVote(@PathVariable Long postId) {
        try {
            List<VoteDTO> dtoList = voteService.viewVotes(postId);
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
            int count = voteService.countVote(postId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "found", count));
        } catch (VoteException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @PostMapping("/insert")
    public ResponseEntity<ResponseObject> insertVote(@RequestBody VoteDTO voteDTO) {
        try {
            VoteDTO dto = voteService.insertVote(voteDTO);
            if (dto != null)
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "Vote have been inserted", dto));
            else
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "Vote have been deleted", ""));
        } catch (VoteException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }
}
