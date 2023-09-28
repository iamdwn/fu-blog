package tech.fublog.FuBlog.dto;

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
public class BlogPostDTO {

    private String typePost;

    private String title;

    private String content;

    private Long category;

    private Long authors;

    private Long views;     //click

//    @CreatedDate
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date date;

    private Date createdDate;

    public LocalDateTime getCreatedDate() {
        ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh"); // Múi giờ Việt Nam
        return createdDate.toInstant().atZone(zoneId).toLocalDateTime();
    }

    public BlogPostDTO(String typePost,
                       String title,
                       String content,
                       Long category,
                       Long authors,
                       Date createdDate,
                       Long views) {
        this.typePost = typePost;
        this.title = title;
        this.content = content;
        this.category = category;
        this.authors = authors;
        this.createdDate = createdDate;
        this.views = views;
    }
}
