package pl.edu.atar.movie.domain;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

class InMemoryMovieRepository implements MovieRepository {

    private final ConcurrentHashMap<Long, Movie> map = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Movie save(Movie movie) {
        Objects.requireNonNull(movie);
        Long id = movie.getMovieId();
        if (id == null) {
            id = idGenerator.getAndIncrement();
            movie = new Movie.Builder()
                    .fromPrototype(movie)
                    .movieId(id)
                    .build();
        }
        map.put(id, movie);
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
    public List<Movie> findAllByOrderByYearDesc() {
        return map.values().stream()
                .sorted(Comparator.comparing(Movie::getYear, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Long movieId) {
        return map.containsKey(movieId);
    }
}
