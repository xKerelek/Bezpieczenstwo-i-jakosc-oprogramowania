package pl.edu.atar.movie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.edu.atar.movie.domain.MovieFacade;
import pl.edu.atar.movie.dto.MovieDto;
import pl.edu.atar.movie.dto.MovieNotFoundException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
class MovieApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieApiController.class);

    private final MovieFacade movieFacade;

    public MovieApiController(MovieFacade movieFacade) {
        this.movieFacade = movieFacade;
    }

    @GetMapping(value = "/movies", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<MovieDto>> getMovies() {
        LOGGER.info("find all movies");

        List<MovieDto> moviesDto = movieFacade.findAll();
        return new ResponseEntity<>(moviesDto, HttpStatus.OK);
    }

    @GetMapping(value = "/movies/{movieId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<MovieDto> getMovieDetails(@PathVariable Long movieId) {
        LOGGER.info("find movie details for id: {}", movieId);

        try {
            MovieDto movieDto = movieFacade.show(movieId);
            return new ResponseEntity<>(movieDto, HttpStatus.OK);
        } catch (MovieNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/movies", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<MovieDto> createMovie(@RequestBody MovieDto movieDto) {
        LOGGER.info("create movie: {}", movieDto);

        MovieDto created = movieFacade.add(movieDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getMovieId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @DeleteMapping(value = "/movies/{movieId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> deleteMovie(@PathVariable Long movieId) {
        LOGGER.info("delete movie: {}", movieId);
        boolean deleted = movieFacade.delete(movieId);

        if (deleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/movies/{movieId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> updateMovie(@PathVariable Long movieId,
                                     @RequestBody MovieDto movieDto) {
        LOGGER.info("update movieId: {}", movieId);
        LOGGER.info("update movie details: {}", movieDto);

        try {
            movieFacade.update(movieId, movieDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (MovieNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
