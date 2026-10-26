package pl.edu.atar.domain.dto;

import com.sun.istack.Builder;

import java.io.Serializable;

public class CreateMovieDto implements Serializable {
    private String title;
    private String image;
    private Integer year;
    private String videoId;

    public CreateMovieDto(){}

    private CreateMovieDto(Builder builder) {
        this.title = builder.title;
        this.image = builder.image;
        this.year = builder.year;
        this.videoId = builder.videoId;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public Integer getYear() {
        return year;
    }

    public String getVideoId() {
        return videoId;
    }

    @Override
    public String toString() {
        return "CreateMovieDto{" +
                "title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", year=" + year +
                ", videoId='" + videoId + '\'' +
                '}';
    }

    public static final class Builder {
        private String title;
        private String image;
        private Integer year;
        private String videoId;

        public Builder() {}

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder image(String image) {
            this.image = image;
            return this;
        }

        public Builder year(Integer year) {
            this.year = year;
            return this;
        }

        public Builder videoId(String videoId) {
            this.videoId = videoId;
            return this;
        }

        public Builder fromPrototype(CreateMovieDto prototype) {
            this.title = prototype.getTitle();
            this.image = prototype.getImage();
            this.year = prototype.getYear();
            this.videoId = prototype.getVideoId();
            return this;
        }
        public CreateMovieDto build() {
            return new CreateMovieDto(this);
        }
    }
}
