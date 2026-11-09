package pl.edu.atar.movie.domain;

import pl.edu.atar.movie.dto.MovieDto;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "movies")
class Movie implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private Long movieId;

    @Column(name = "title")
    private String title;

    @Column(name = "image")
    private String image;

    @Column(name = "publication_year")
    private Integer year;

    @Column(name = "video_id")
    private String videoId;

    protected Movie() {
    }

    private Movie(Builder builder) {
        this.movieId = builder.movieId;
        this.title = builder.title;
        this.image = builder.image;
        this.year = builder.year;
        this.videoId = builder.videoId;
    }

    Long getMovieId() {
        return movieId;
    }

    String getTitle() {
        return title;
    }

    String getImage() {
        return image;
    }

    Integer getYear() {
        return year;
    }

    String getVideoId() {
        return videoId;
    }

    MovieDto dto() {
        return new MovieDto.Builder()
                .movieId(movieId)
                .title(title)
                .image(image)
                .year(year)
                .videoId(videoId)
                .build();
    }

    static final class Builder {
        private Long movieId;
        private String title;
        private String image;
        private Integer year;
        private String videoId;

        Builder() {
        }

        Builder movieId(Long movieId) {
            this.movieId = movieId;
            return this;
        }

        Builder title(String title) {
            this.title = title;
            return this;
        }

        Builder image(String image) {
            this.image = image;
            return this;
        }

        Builder year(Integer year) {
            this.year = year;
            return this;
        }

        Builder videoId(String videoId) {
            this.videoId = videoId;
            return this;
        }

        Builder fromPrototype(Movie prototype) {
            this.movieId = prototype.getMovieId();
            this.title = prototype.getTitle();
            this.image = prototype.getImage();
            this.year = prototype.getYear();
            this.videoId = prototype.getVideoId();
            return this;
        }

        Movie build() {
            return new Movie(this);
        }
    }
}
