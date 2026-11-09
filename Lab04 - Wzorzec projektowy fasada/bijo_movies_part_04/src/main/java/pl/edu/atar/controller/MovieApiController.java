package pl.edu.atar.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.edu.atar.movie.domain.MovieFacade;
import pl.edu.atar.movie.dto.MovieDto;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api")
public class MovieApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieApiController.class);

    private final MovieFacade movieFacade;

    @Autowired
    public MovieApiController(MovieFacade movieFacade) {
        this.movieFacade = movieFacade;
    }

    @GetMapping(value = "/movies", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MovieDto>> getMovies() {
        LOGGER.info("find all movies");

        List<MovieDto> moviesDto = movieFacade.findAll();
        return new ResponseEntity<>(moviesDto, HttpStatus.OK);
    }

    @GetMapping(value = "/movies/{movieId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MovieDto> getMovieDetails(@PathVariable Long movieId) {
        LOGGER.info("find movie details for id: {}", movieId);

        Optional<MovieDto> movieDetails = movieFacade.findMovieDetailsById(movieId);
        return movieDetails
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/movies", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MovieDto> createMovie(@RequestBody MovieDto movieDto) {
        LOGGER.info("create movie: {}", movieDto);

        Long movieId = movieFacade.createMovie(movieDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(movieId)
                .toUri();

        return ResponseEntity.created(location).body(movieDto);
    }

    @DeleteMapping(value = "/movies/{movieId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteMovie(@PathVariable Long movieId) {
        LOGGER.info("delete movie: {}", movieId);
        boolean deleted = movieFacade.deleteMovie(movieId);

        if (deleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/movies/{movieId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateMovie(@PathVariable Long movieId,
                                            @RequestBody MovieDto movieDto) {
        LOGGER.info("update movieId: {}", movieId);
        LOGGER.info("update movie details: {}", movieDto);

        boolean updated = movieFacade.updateMovie(movieId, movieDto);
        if (updated) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
