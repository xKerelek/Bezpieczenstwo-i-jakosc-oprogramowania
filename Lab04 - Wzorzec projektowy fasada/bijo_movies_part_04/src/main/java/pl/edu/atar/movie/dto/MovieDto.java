package pl.edu.atar.movie.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.io.Serializable;
import java.util.Objects;

@JsonDeserialize(builder = MovieDto.Builder.class)
public class MovieDto implements Serializable {

    private Long movieId;
    private String title;
    private String image;
    private Integer year;
    private String videoId;

    private MovieDto(Builder builder) {
        movieId = builder.movieId;
        title = builder.title;
        image = builder.image;
        year = builder.year;
        videoId = builder.videoId;
    }

    public Long getMovieId() {
        return movieId;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieDto movieDto = (MovieDto) o;
        return Objects.equals(movieId, movieDto.movieId) &&
                Objects.equals(title, movieDto.title) &&
                Objects.equals(image, movieDto.image) &&
                Objects.equals(year, movieDto.year) &&
                Objects.equals(videoId, movieDto.videoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieId, title, image, year, videoId);
    }

    @Override
    public String toString() {
        return "MovieDto{" +
                "movieId=" + movieId +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", year=" + year +
                ", videoId='" + videoId + '\'' +
                '}';
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static final class Builder {
        private Long movieId;
        private String title;
        private String image;
        private Integer year;
        private String videoId;

        public Builder() {
        }

        public Builder movieId(Long movieId) {
            this.movieId = movieId;
            return this;
        }

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

        public Builder fromPrototype(MovieDto prototype) {
            this.movieId = prototype.getMovieId();
            this.title = prototype.getTitle();
            this.image = prototype.getImage();
            this.year = prototype.getYear();
            this.videoId = prototype.getVideoId();
            return this;
        }

        public MovieDto build() {
            return new MovieDto(this);
        }
    }
}
