package pl.edu.atar.movie.dto;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MovieNotFoundException extends RuntimeException {

    public MovieNotFoundException(Long movieId) {
        super("No movie of id " + movieId + " found", null, false, false);
    }

    public MovieNotFoundException(String message) {
        super(message, null, false, false);
    }
}
