package pl.edu.atar.domain.mapper;

import org.springframework.stereotype.Component;
import pl.edu.atar.domain.dto.CreateMovieDto;
import pl.edu.atar.domain.entity.Movie;

@Component
public class MovieMapper implements Converter<Movie, CreateMovieDto> {

    @Override
    public Movie convert(CreateMovieDto from) {
        return new Movie.Builder()
                .title(from.getTitle())
                .image(from.getImage())
                .year(from.getYear())
                .videoId(from.getVideoId())
                .build();
    }
}