package tech.fublog.FuBlog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import tech.fublog.FuBlog.dto.request.AwardRequestDTO;
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

    @Autowired
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

        List<String> roles = jwtService.extractTokenToGetRoles(token.substring(7));
        if (roles != null) {
            if (!roles.isEmpty()) {
                for (String role : roles) {
                    if (!role.toUpperCase().equals("USER")) {
                        return awardService.awardPrize(awardRequestDTO);
                    }
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseObject("ok", "Role is not sp!!", ""));
                }
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "Role is empty!!", ""));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "Role is null!!", ""));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("failed", "token is wrong or role is not sp!!", ""));

    }


}
