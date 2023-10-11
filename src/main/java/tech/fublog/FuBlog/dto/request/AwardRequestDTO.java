package tech.fublog.FuBlog.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Data
@NoArgsConstructor
//@AllArgsConstructor
public class AwardRequestDTO {

    private String awardName;
    private Long userId;
    private Date createdDate;

    public LocalDateTime getCreatedDate() {
        ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh"); // Múi giờ Việt Nam
        return createdDate.toInstant().atZone(zoneId).toLocalDateTime();
    }

    public AwardRequestDTO(String awardName, Long userId) {
        this.awardName = awardName;
        this.userId = userId;
    }
}
