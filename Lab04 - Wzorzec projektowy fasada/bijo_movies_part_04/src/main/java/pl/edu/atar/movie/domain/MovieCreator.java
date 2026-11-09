package pl.edu.atar.movie.domain;

import pl.edu.atar.movie.dto.MovieDto;

import java.util.Objects;

class MovieCreator {

    Movie from(MovieDto movieDto) {
        Objects.requireNonNull(movieDto);
        return new Movie.Builder()
                .movieId(movieDto.getMovieId())
                .title(movieDto.getTitle())
                .image(movieDto.getImage())
                .year(movieDto.getYear())
                .videoId(movieDto.getVideoId())
                .build();
    }

    Movie from(Long movieId, MovieDto movieDto) {
        Objects.requireNonNull(movieId);
        Objects.requireNonNull(movieDto);
        return new Movie.Builder()
                .movieId(movieId)
                .title(movieDto.getTitle())
                .image(movieDto.getImage())
                .year(movieDto.getYear())
                .videoId(movieDto.getVideoId())
                .build();
    }
}
