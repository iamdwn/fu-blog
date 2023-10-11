package tech.fublog.FuBlog.controller;

import tech.fublog.FuBlog.dto.request.AwardRequestDTO;
import tech.fublog.FuBlog.model.ResponseObject;
import tech.fublog.FuBlog.service.AwardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth/award")
@CrossOrigin(origins = {"http://localhost:5173", "https://fublog.tech"})
//@CrossOrigin(origins = "*")
public class  AwardController {

    @Autowired
    private AwardService awardService;

    @GetMapping("viewAwards")
    ResponseEntity<ResponseObject> getAllAwards() {

        return awardService.getAllAwards();
    }

    @PostMapping("awardPrize")
    ResponseEntity<ResponseObject> awardPrize(
            @RequestBody AwardRequestDTO awardRequestDTO) {

        return awardService.awardPrize(awardRequestDTO);
    }



}
