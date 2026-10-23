package pl.edu.atar.domain.mapper;

import org.springframework.stereotype.Component;
import pl.edu.atar.domain.dto.MovieDto;
import pl.edu.atar.domain.entity.Movie;

import java.util.ArrayList;
import java.util.List;

import java.util.stream.Collectors;

@Component
public class MovieListMapper implements Converter<List<MovieDto>, List<Movie>> {

    @Override
    public List<MovieDto> convert(List<Movie> from) {
        if (from == null) {
            return new ArrayList<>();
        }

        return from.stream()
                .map(movie -> {
                    MovieDto movieDto = new MovieDto();
                    movieDto.setMovieId(movie.getMovieId());
                    movieDto.setTitle(movie.getTitle());
                    movieDto.setImage(movie.getImage());
                    movieDto.setYear(movie.getYear());
                    return movieDto;
                })
                .collect(Collectors.toList());
    }

    public List<MovieDto> mapToDto(List<Movie> movies) {
        return convert(movies);
    }
}
