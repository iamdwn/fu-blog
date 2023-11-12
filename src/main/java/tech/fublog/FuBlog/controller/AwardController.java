package tech.fublog.FuBlog.controller;

import org.springframework.http.HttpStatus;
import tech.fublog.FuBlog.Utility.TokenChecker;
import tech.fublog.FuBlog.dto.request.AwardRequestDTO;
import tech.fublog.FuBlog.entity.BlogPostEntity;
import tech.fublog.FuBlog.model.ResponseObject;
import tech.fublog.FuBlog.service.AwardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.fublog.FuBlog.service.JwtService;

import java.util.List;


@RestController
@RequestMapping("/api/v1/auth/award")
@CrossOrigin(origins = {"http://localhost:5173", "https://fublog.tech"})
//@CrossOrigin(origins = "*")
public class AwardController {
    private final AwardService awardService;
    private final JwtService jwtService;

    public AwardController(AwardService awardService, JwtService jwtService) {
        this.awardService = awardService;
        this.jwtService = jwtService;
    }

    @GetMapping("/viewAwards")
    ResponseEntity<ResponseObject> getAllAwards() {

        return awardService.getAllAwards();
    }

    @PostMapping("/awardPrize")
    ResponseEntity<ResponseObject> awardPrize(@RequestHeader("Authorization") String token,
                                              @RequestBody AwardRequestDTO awardRequestDTO) {

        try {
            if (TokenChecker.checkToken(token))
                return awardService.awardPrize(awardRequestDTO);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

}
