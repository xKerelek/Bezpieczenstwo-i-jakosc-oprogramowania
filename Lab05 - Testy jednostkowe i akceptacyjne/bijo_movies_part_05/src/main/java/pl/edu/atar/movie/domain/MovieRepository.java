package pl.edu.atar.movie.domain;

import org.springframework.data.repository.Repository;
import pl.edu.atar.movie.dto.MovieNotFoundException;

import java.util.List;
import java.util.Optional;

interface MovieRepository extends Repository<Movie, Long> {

    Movie save(Movie movie);

    Optional<Movie> findById(Long movieId);

    void deleteById(Long movieId);

    List<Movie> findAllByOrderByPublicationYearDesc();

    default Movie findOneOrThrow(Long movieId) {
        return findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException(movieId));
    }

    void deleteAll();
}
