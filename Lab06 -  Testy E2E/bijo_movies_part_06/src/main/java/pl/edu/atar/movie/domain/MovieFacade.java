package pl.edu.atar.movie.domain;

import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.atar.movie.dto.MovieDto;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Transactional
@AllArgsConstructor
public class MovieFacade {

    private final MovieRepository movieRepository;
    private final MovieCreator movieCreator;

    public MovieDto add(MovieDto movieDto) {
        requireNonNull(movieDto);

        Movie movie = movieCreator.from(movieDto);
        movie = movieRepository.save(movie);
        return movie.dto();
    }

    public MovieDto show(Long movieId) {
        requireNonNull(movieId);
        Movie movie = movieRepository.findOneOrThrow(movieId);
        return movie.dto();
    }

    public List<MovieDto> findAll() {
        return movieRepository.findAllByOrderByPublicationYearDesc()
                .stream()
                .map(Movie::dto)
                .collect(Collectors.toList());
    }

    public MovieDto update(Long movieId, MovieDto movieDto) {
        requireNonNull(movieId);
        requireNonNull(movieDto);

        movieRepository.findOneOrThrow(movieId);
        movieDto.setMovieId(movieId);
        Movie movie = movieCreator.from(movieDto);
        return movieRepository.save(movie).dto();
    }

    public boolean delete(Long movieId) {
        requireNonNull(movieId);

        if (movieRepository.findById(movieId).isPresent()) {
            movieRepository.deleteById(movieId);
            return true;
        }

        return false;
    }

    public void deleteAll() {
        movieRepository.deleteAll();
    }
}
