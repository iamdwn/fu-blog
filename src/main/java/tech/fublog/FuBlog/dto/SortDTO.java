package tech.fublog.FuBlog.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Data
@Getter
@Setter
public class SortDTO {
    private String image;
    private String title;
    private Date createdDate;

    public SortDTO(String image, String title, Date createdDate) {
        this.image = image;
        this.title = title;
        this.createdDate = createdDate;
    }
}
