package pl.edu.atar.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.edu.atar.movie.domain.MovieFacade;
import pl.edu.atar.movie.dto.MovieDto;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MovieApiControllerTest {

    private MovieFacade movieFacade;
    private MovieApiController controller;

    @BeforeEach
    void setUp() {
        movieFacade = mock(MovieFacade.class);
        controller = new MovieApiController(movieFacade);
    }

    @Test
    void shouldReturn200OkWhenUpdateMovieSucceeds() {
        // given
        Long movieId = 1L;
        MovieDto movieDto = new MovieDto.Builder()
                .title("Zaktualizowany tytuł")
                .year(2022)
                .build();
        when(movieFacade.updateMovie(movieId, movieDto)).thenReturn(true);

        // when
        ResponseEntity<Void> response = controller.updateMovie(movieId, movieDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(movieFacade).updateMovie(movieId, movieDto);
    }

    @Test
    void shouldReturn404NotFoundWhenMovieToUpdateDoesNotExist() {
        // given
        Long movieId = 999L;
        MovieDto movieDto = new MovieDto.Builder()
                .title("Nieistniejący")
                .year(2022)
                .build();
        when(movieFacade.updateMovie(movieId, movieDto)).thenReturn(false);

        // when
        ResponseEntity<Void> response = controller.updateMovie(movieId, movieDto);

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(movieFacade).updateMovie(movieId, movieDto);
    }
}
