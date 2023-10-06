package tech.fublog.FuBlog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tech.fublog.FuBlog.dto.request.AwardDTO;
import tech.fublog.FuBlog.entity.AwardEntity;
import tech.fublog.FuBlog.entity.UserAwardEntity;
import tech.fublog.FuBlog.entity.UserEntity;
import tech.fublog.FuBlog.model.ResponseObject;
import tech.fublog.FuBlog.repository.AwardRepository;
import tech.fublog.FuBlog.repository.UserAwardRepository;
import tech.fublog.FuBlog.repository.UserRepository;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AwardService {

    @Autowired
    private AwardRepository awardRepository;

    @Autowired
    private UserAwardRepository userAwardRepository;

    @Autowired
    private UserRepository userRepository;


    public ResponseEntity<ResponseObject> getAllAwards() {
        List<AwardEntity> awardEntity = awardRepository.findAll();
        return !awardEntity.isEmpty() ? ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("ok", "list found", awardEntity))

                : ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseObject("failed", "not found", ""));
    }


    public ResponseEntity<ResponseObject> awardPrize(AwardDTO awardDTO) {
        Optional<AwardEntity> awardEntity = awardRepository.findByName(awardDTO.getAwardName());
        Optional<UserEntity> userEntity = userRepository.findById(awardDTO.getUserId());


        if (awardEntity.isPresent()){
            Date achievementDate = new Date();
            ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
            achievementDate.toInstant().atZone(zoneId).toLocalDateTime();

            UserAwardEntity userAward = new UserAwardEntity(achievementDate, userEntity.get(), awardEntity.get());

            userAwardRepository.save(userAward);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "awarded successful", ""));
        }


        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .body(new ResponseObject("failed", "awarded failed", ""));
    }

}
