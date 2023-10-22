package tech.fublog.FuBlog.service;

import org.springframework.stereotype.Service;
import tech.fublog.FuBlog.dto.request.UserReportDTO;
import tech.fublog.FuBlog.entity.UserEntity;
import tech.fublog.FuBlog.entity.UserReportEntity;
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

    public boolean createReport(UserReportDTO userReportDTO) {
        Optional<UserEntity> reporterUser = userRepository.findById(userReportDTO.getReporterId());
        Optional<UserEntity> reportedUser = userRepository.findById(userReportDTO.getReportedUserId());
        if (reportedUser.isPresent()
                && reporterUser.isPresent()) {
            UserReportEntity userReportEntity = new UserReportEntity(userReportDTO.getReason(), reporterUser.get(), reportedUser.get());
            userReportRepository.save(userReportEntity);
            return true;
        }
        return false;
    }

    public List<UserReportDTO> getAllReport() {
        List<UserReportEntity> entityList = userReportRepository.findAll();
        if (!entityList.isEmpty()) {
            List<UserReportDTO> dtoList = new ArrayList<>();
            for (UserReportEntity entity : entityList) {
                dtoList.add(DTOConverter.convertUserReportDTO(entity));
            }
            return dtoList;
        }
        return new ArrayList<>();
    }
}
