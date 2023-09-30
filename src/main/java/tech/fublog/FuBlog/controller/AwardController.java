package tech.fublog.FuBlog.controller;

import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import tech.fublog.FuBlog.dto.AwardDTO;
import tech.fublog.FuBlog.dto.BlogPostDTO;
import tech.fublog.FuBlog.dto.SortDTO;
import tech.fublog.FuBlog.entity.BlogPostEntity;
import tech.fublog.FuBlog.model.ResponseObject;
import tech.fublog.FuBlog.service.ApprovalRequestService;
import tech.fublog.FuBlog.service.AwardService;
import tech.fublog.FuBlog.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.fublog.FuBlog.service.CategoryService;

import java.util.List;


@RestController
@RequestMapping("/api/v1/auth")
//@CrossOrigin(origins = "*")
public class  AwardController {

    @Autowired
    private AwardService awardService;

    @GetMapping("/award/viewAwards")
    ResponseEntity<ResponseObject> getAllAwards() {

        return awardService.getAllAwards();
    }

    @PostMapping("/award/awardPrize")
    ResponseEntity<ResponseObject> awardPrize(
            @RequestBody AwardDTO awardDTO) {

        return awardService.awardPrize(awardDTO);
    }



}
