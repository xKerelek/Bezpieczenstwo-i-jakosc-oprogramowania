package pl.edu.atar.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.edu.atar.domain.dto.CreateMovieDto;
import pl.edu.atar.domain.dto.MovieDetailsDto;
import pl.edu.atar.domain.dto.MovieDto;
import pl.edu.atar.service.MovieService;

import java.net.URI;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(value="/api")
public class MovieApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieApiController.class);

    private final MovieService movieService;

    @Autowired
    public MovieApiController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping(value = "/movies", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MovieDto>> getMovies() {
        LOGGER.info("find all movies");

        List<MovieDto> moviesDto = movieService.findAll();
        return new ResponseEntity<>(moviesDto, HttpStatus.OK);
    }

    @GetMapping(value = "/movies/{movieId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MovieDetailsDto> getMovieDetails(@PathVariable Long movieId) {
        LOGGER.info("find movie details for id: {}", movieId);

        Optional<MovieDetailsDto> movieDetails = movieService.findMovieDetailsById(movieId);
        return movieDetails
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/movies", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateMovieDto> createMovie(@RequestBody CreateMovieDto createMovieDto) {
        LOGGER.info("create movie: {}", createMovieDto);

        Long movieId = movieService.createMovie(createMovieDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(movieId)
                .toUri();

        return ResponseEntity.created(location).body(createMovieDto);
    }

    @DeleteMapping(value = "/movies/{movieId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteMovie(@PathVariable Long movieId) {
        LOGGER.info("delete movie: {}", movieId);
        boolean deleted = movieService.deleteMovie(movieId);

        if (deleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
