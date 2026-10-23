package pl.edu.atar.service;

import pl.edu.atar.domain.dto.CreateMovieDto;
import pl.edu.atar.domain.dto.MovieDto;

import java.util.List;

public interface MovieService {

    List<MovieDto> findAll();

    void create(CreateMovieDto createMovieDto);

    void delete(Long id);
}
