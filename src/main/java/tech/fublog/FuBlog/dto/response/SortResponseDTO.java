package tech.fublog.FuBlog.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Data
@Getter
@Setter
public class SortResponseDTO {
    private String picture;
    private String title;
    private Date createdDate;

    public SortResponseDTO(String picture, String title, Date createdDate) {
        this.picture = picture;
        this.title = title;
        this.createdDate = createdDate;
    }
}
