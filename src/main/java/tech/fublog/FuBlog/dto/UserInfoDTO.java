package tech.fublog.FuBlog.dto;

public class UserInfoDTO {
    private String name;
    private String picture;
    private Double point;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture  ;
    }

    public Double getPoint() {
        return point;
    }

    public void setPoint(Double point) {
        this.point = point;
    }

    public UserInfoDTO() {
    }

    public UserInfoDTO(String name, String picture, Double point) {
        this.name = name;
        this.picture = picture;
        this.point = point;
    }
}
