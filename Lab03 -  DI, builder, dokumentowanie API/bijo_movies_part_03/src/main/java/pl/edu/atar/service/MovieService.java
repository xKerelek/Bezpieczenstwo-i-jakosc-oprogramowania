package pl.edu.atar.service;

import pl.edu.atar.domain.dto.CreateMovieDto;
import pl.edu.atar.domain.dto.MovieDetailsDto;
import pl.edu.atar.domain.dto.MovieDto;

import java.util.List;
import java.util.Optional;

public interface MovieService {

    List<MovieDto> findAll();

    Optional<MovieDetailsDto> findMovieDetailsById(Long movieId);

    Long createMovie(CreateMovieDto createMovieDto);

    boolean deleteMovie(Long movieId);
}
