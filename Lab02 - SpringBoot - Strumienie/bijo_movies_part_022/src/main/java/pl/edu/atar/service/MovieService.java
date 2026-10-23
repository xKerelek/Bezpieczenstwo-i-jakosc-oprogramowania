package pl.edu.atar.service;

import pl.edu.atar.domain.dto.CreateMovieDto;
import pl.edu.atar.domain.dto.MovieDetailsDto;
import pl.edu.atar.domain.dto.MovieDto;

import java.util.List;

public interface MovieService {

    List<MovieDto> findAll();

    Long createMovie(CreateMovieDto createMovieDto);

    void deleteMovie(Long movieId);

    MovieDetailsDto getMovieDetails(Long movieId);
}
