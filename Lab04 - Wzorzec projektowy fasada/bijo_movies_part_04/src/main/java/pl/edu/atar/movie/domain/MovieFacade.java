package pl.edu.atar.movie.domain;

import pl.edu.atar.movie.dto.MovieDto;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
public class MovieFacade {

    private final MovieRepository movieRepository;
    private final MovieCreator movieCreator;

    public MovieFacade(MovieRepository movieRepository, MovieCreator movieCreator) {
        this.movieRepository = movieRepository;
        this.movieCreator = movieCreator;
    }

    public List<MovieDto> findAll() {
        return movieRepository.findAllByOrderByYearDesc().stream()
                .map(Movie::dto)
                .collect(Collectors.toList());
    }

    public Optional<MovieDto> findMovieDetailsById(Long movieId) {
        if (movieId == null) {
            return Optional.empty();
        }
        return movieRepository.findById(movieId).map(Movie::dto);
    }

    public MovieDto show(Long movieId) {
        Objects.requireNonNull(movieId);
        return movieRepository.findOneOrThrow(movieId).dto();
    }

    public Long createMovie(MovieDto movieDto) {
        Objects.requireNonNull(movieDto);
        Movie movie = movieCreator.from(movieDto);
        Movie savedMovie = movieRepository.save(movie);
        return savedMovie.getMovieId();
    }

    public boolean updateMovie(Long movieId, MovieDto movieDto) {
        Objects.requireNonNull(movieId);
        Objects.requireNonNull(movieDto);

        Optional<Movie> existingMovieOptional = movieRepository.findById(movieId);
        if (existingMovieOptional.isPresent()) {
            Movie updatedMovie = movieCreator.from(movieId, movieDto);
            movieRepository.save(updatedMovie);
            return true;
        }
        return false;
    }

    public boolean deleteMovie(Long movieId) {
        Objects.requireNonNull(movieId);
        if (movieRepository.existsById(movieId)) {
            movieRepository.deleteById(movieId);
            return true;
        }
        return false;
    }
}
