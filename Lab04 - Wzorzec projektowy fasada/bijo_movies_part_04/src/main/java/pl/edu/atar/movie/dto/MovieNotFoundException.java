package pl.edu.atar.movie.dto;

public class MovieNotFoundException extends RuntimeException {
    public MovieNotFoundException(Long movieId) {
        super("No movie of id " + movieId + " found", null, false, false);
    }
}
