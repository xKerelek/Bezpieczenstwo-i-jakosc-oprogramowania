package pl.edu.atar.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.atar.domain.entity.Movie;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>, CrudRepository<Movie, Long> {

    List<Movie> findAllByOrderByYearDesc();

    Optional<Movie> findByMovieId(Long movieId);
}
