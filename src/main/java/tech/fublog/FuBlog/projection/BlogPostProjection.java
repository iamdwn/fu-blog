package tech.fublog.FuBlog.projection;

import tech.fublog.FuBlog.dto.TagDTO;
import tech.fublog.FuBlog.dto.UserDTO;
import tech.fublog.FuBlog.entity.BlogPostEntity;
import tech.fublog.FuBlog.entity.CategoryEntity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

public interface BlogPostProjection {
    Long getId();
    String getTitle();
    LocalDateTime getCreatedDate();
    Long getView();
    // Thêm các trường cần thiết khác
}