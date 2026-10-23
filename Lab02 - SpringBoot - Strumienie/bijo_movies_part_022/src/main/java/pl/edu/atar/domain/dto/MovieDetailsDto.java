package pl.edu.atar.domain.dto;

public class MovieDetailsDto {
    private String title;
    private String videoId;

    public MovieDetailsDto() {
        
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getvideoId() {
        return videoId;
    }

    public void setvideoId(String videoId) {
        this.videoId = videoId;
    }
}
