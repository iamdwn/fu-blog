package tech.fublog.FuBlog.service;

import tech.fublog.FuBlog.dto.ApprovalRequestDTO;
import tech.fublog.FuBlog.entity.ApprovalRequestEntity;
import tech.fublog.FuBlog.entity.BlogPostEntity;
import tech.fublog.FuBlog.repository.ApprovalRequestRepository;
import tech.fublog.FuBlog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApprovalRequestService {
    private final ApprovalRequestRepository approvalRequestRepository;

    private final UserRepository userRepository;

    @Autowired
    public ApprovalRequestService(ApprovalRequestRepository approvalRequestRepository, UserRepository userRepository) {
        this.approvalRequestRepository = approvalRequestRepository;
        this.userRepository = userRepository;
    }

    public List<ApprovalRequestDTO> getAllApprovalRequest() {
        List<ApprovalRequestEntity> list = approvalRequestRepository.findAll();
        List<ApprovalRequestDTO> dtoList = new ArrayList<>();
        for (ApprovalRequestEntity entity : list) {
            ApprovalRequestDTO dto =
                    new ApprovalRequestDTO(entity.isApproved(), entity.getBlogPost().getId(), entity.getRequest().getId(), /*entity.getReview().getId()*/null);
            dtoList.add(dto);
        }
        return dtoList;
    }

    //    public ResponseEntity<ResponseObject> insertApprovalRequest()
    public void insertApprovalRequest(BlogPostEntity blogPostEntity) {
        ApprovalRequestEntity approvalRequestEntity = new ApprovalRequestEntity(blogPostEntity);
        approvalRequestRepository.save(approvalRequestEntity);
    }
}
