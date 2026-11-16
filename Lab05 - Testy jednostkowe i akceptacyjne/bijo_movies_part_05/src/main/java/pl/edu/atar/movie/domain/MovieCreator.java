package pl.edu.atar.movie.domain;

import pl.edu.atar.movie.dto.MovieDto;

import static java.util.Objects.requireNonNull;

class MovieCreator {

    Movie from(MovieDto movieDto) {
        requireNonNull(movieDto);
        return Movie.builder()
                .movieId(movieDto.getMovieId())
                .title(movieDto.getTitle())
                .image(movieDto.getImage())
                .publicationYear(movieDto.getYear())
                .videoId(movieDto.getVideoId())
                .build();
    }
}
