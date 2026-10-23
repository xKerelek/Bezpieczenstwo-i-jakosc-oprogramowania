package pl.edu.atar.domain.dto;

public class MovieDetailDto {
    private String title;
    private String videoId;

    public MovieDetailDto() {}


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
