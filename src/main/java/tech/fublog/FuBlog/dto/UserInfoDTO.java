package tech.fublog.FuBlog.dto;

public class UserInfoDTO {
    private String name;

    private Double point;

    private String picture;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String imagePath) {
        this.picture = imagePath;
    }

    public Double getPoint() {
        return point;
    }

    public void setPoint(Double point) {
        this.point = point;
    }

    public UserInfoDTO() {
    }

    public UserInfoDTO(String name, String imagePath, Double point) {
        this.name = name;
        this.point = point;
        this.picture = imagePath;
    }
}
