package tech.fublog.FuBlog.service;

import org.springframework.stereotype.Service;
import tech.fublog.FuBlog.dto.request.UserReportDTO;
import tech.fublog.FuBlog.entity.UserEntity;
import tech.fublog.FuBlog.entity.UserReportEntity;
import tech.fublog.FuBlog.exception.BlogPostReportException;
import tech.fublog.FuBlog.exception.UserReportException;
import tech.fublog.FuBlog.repository.UserReportRepository;
import tech.fublog.FuBlog.repository.UserRepository;
import tech.fublog.FuBlog.Utility.DTOConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserReportService {
    private final UserReportRepository userReportRepository;
    private final UserRepository userRepository;

    public UserReportService(UserReportRepository userReportRepository,
                             UserRepository userRepository) {
        this.userReportRepository = userReportRepository;
        this.userRepository = userRepository;
    }

    public void createReportUser(UserReportDTO userReportDTO) {
        Optional<UserEntity> reporterUser = userRepository.findById(userReportDTO.getReporterId());
        Optional<UserEntity> reportedUser = userRepository.findById(userReportDTO.getReportedUserId());
        if (userReportDTO.getReporterId().equals(userReportDTO.getReportedUserId()))
            throw new UserReportException("You can not report yourself");
        if (reportedUser.isPresent()
                && reporterUser.isPresent()) {
            UserReportEntity userReportEntity = userReportRepository.findByReporterIdAndReportedUserId(reporterUser.get(), reportedUser.get());
            if (userReportEntity == null) {
                userReportEntity = new UserReportEntity(userReportDTO.getReason(), reporterUser.get(), reportedUser.get());
                userReportRepository.save(userReportEntity);
            } else throw new UserReportException("You already report this user");
        } else throw new UserReportException("User doesn't valid");
    }

    public List<UserReportDTO> viewAllReportUser() {
        List<UserReportEntity> entityList = userReportRepository.findByOrderByCreatedDateDesc();
        List<UserReportDTO> dtoList = new ArrayList<>();
        if (!entityList.isEmpty()) {
            for (UserReportEntity entity : entityList) {
                dtoList.add(DTOConverter.convertUserReportDTO(entity));
            }
        }
        return dtoList;
    }

    public boolean checkReport(Long reporterId, Long reportedId) {
        boolean result = false;
        Optional<UserEntity> reporterUser = userRepository.findById(reporterId);
        Optional<UserEntity> reportedUser = userRepository.findById(reportedId);
        if (reporterUser.isPresent()
                && reportedUser.isPresent()) {
            UserReportEntity userReportEntity = userReportRepository.findByReporterIdAndReportedUserId(reporterUser.get(), reportedUser.get());
            if (userReportEntity != null) {
                result = true;
            }
            return result;
        } else throw new BlogPostReportException("User or Blog doesn't valid!");
    }
}
