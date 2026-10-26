package pl.edu.atar.domain.dto;

import com.sun.istack.Builder;

import java.io.Serializable;

public class MovieDetailsDto implements Serializable {

    private String title;
    private String videoId;

    public MovieDetailsDto() {
    }

    private MovieDetailsDto(String title, String videoId) {
        this.title = title;
        this.videoId = videoId;
    }

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

    public static final class Builder(Builder builder) {
        private String title;
        private String videoId;

        public Builder(){}

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder videoId(String videoId) {
            this.videoId = videoId;
            return this;
        }

        public Builder fromPrototype(MovieDetailsDto prototype) {
            this.title = prototype.getTitle();
            this.videoId = prototype.getVideoId();
            return this;
        }
        public MovieDetailsDto build() {
            return new MovieDetailsDto(this);
        }
    }
}
