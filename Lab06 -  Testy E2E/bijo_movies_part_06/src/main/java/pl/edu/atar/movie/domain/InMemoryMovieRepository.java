package pl.edu.atar.movie.domain;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

class InMemoryMovieRepository implements MovieRepository {

    private final ConcurrentHashMap<Long, Movie> map = new ConcurrentHashMap<>();

    @Override
    public Movie save(Movie movie) {
        requireNonNull(movie);
        map.put(movie.dto().getMovieId(), movie);
        return movie;
    }

    @Override
    public Optional<Movie> findById(Long movieId) {
        return Optional.ofNullable(map.get(movieId));
    }

    @Override
    public void deleteById(Long movieId) {
        map.remove(movieId);
    }

    @Override
    public List<Movie> findAllByOrderByPublicationYearDesc() {
        return map.values().stream()
                .sorted(Comparator.comparing(Movie::getPublicationYear, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAll() {
        map.clear();
    }
}
