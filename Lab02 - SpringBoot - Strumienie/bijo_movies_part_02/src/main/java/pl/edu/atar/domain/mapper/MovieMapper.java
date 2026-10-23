package pl.edu.atar.domain.mapper;

import org.springframework.stereotype.Component;
import pl.edu.atar.domain.dto.CreateMovieDto;
import pl.edu.atar.domain.dto.MovieDto;
import pl.edu.atar.domain.entity.Movie;

import java.util.List;

@Component
public class MovieMapper implements Converter<Movie, CreateMovieDto> {

    @Override
    public Movie convert(CreateMovieDto createMovieDto) {
        Movie movie = new Movie();

        movie.setTitle(createMovieDto.getTitle());
        movie.setImage(createMovieDto.getImage());
        movie.setYear(createMovieDto.getYear());
        movie.setVideoId(createMovieDto.getVideoId());

        return movie;
    }

    public Movie mapToEntity(CreateMovieDto createMovieDto) {
        return convert(createMovieDto);
    }
}
