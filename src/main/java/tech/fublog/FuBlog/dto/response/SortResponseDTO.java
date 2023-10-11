package tech.fublog.FuBlog.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Data
@Getter
@Setter
public class SortResponseDTO {
    private String image;
    private String title;
    private Date createdDate;

    public SortResponseDTO(String image, String title, Date createdDate) {
        this.image = image;
        this.title = title;
        this.createdDate = createdDate;
    }
}
