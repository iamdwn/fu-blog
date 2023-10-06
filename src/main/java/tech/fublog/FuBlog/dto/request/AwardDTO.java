package tech.fublog.FuBlog.dto.request;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Data
@NoArgsConstructor
//@AllArgsConstructor
public class AwardDTO {

    private String awardName;
    private Long userId;
    private Date createdDate;

    public LocalDateTime getCreatedDate() {
        ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh"); // Múi giờ Việt Nam
        return createdDate.toInstant().atZone(zoneId).toLocalDateTime();
    }

    public AwardDTO(String awardName, Long userId) {
        this.awardName = awardName;
        this.userId = userId;
    }
}
