package tech.fublog.FuBlog.dto;

public class ImageDTO {

    private Long id;
    private String name;
    private String url;

    public ImageDTO() {
    }

    public ImageDTO(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}