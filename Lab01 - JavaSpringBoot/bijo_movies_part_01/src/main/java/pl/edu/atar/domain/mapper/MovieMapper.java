package pl.edu.atar.domain.mapper;

import org.springframework.stereotype.Component;
import pl.edu.atar.domain.dto.CreateMovieDto;
import pl.edu.atar.domain.entity.Movie;

@Component
public class MovieMapper {
    public Movie mapToEntity(CreateMovieDto createMovieDto) {
        Movie movie = new Movie();
        movie.setTitle(createMovieDto.getTitle());
        movie.setImage(createMovieDto.getImage());
        movie.setYear(createMovieDto.getYear());

        return movie;
    }
}
