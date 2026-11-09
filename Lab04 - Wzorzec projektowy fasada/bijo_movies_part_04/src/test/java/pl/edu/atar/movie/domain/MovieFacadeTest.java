package pl.edu.atar.movie.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.atar.movie.dto.MovieDto;
import pl.edu.atar.movie.dto.MovieNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MovieFacadeTest {

    private MovieFacade movieFacade;

    @BeforeEach
    void setUp() {
        // Tworzymy instancję fasady w oparciu o InMemoryMovieRepository (bez Springa i bez bazy danych)
        movieFacade = new MovieConfiguration().movieFacade();
    }

    @Test
    void shouldAddAndFindMovie() {
        // given
        MovieDto movieDto = new MovieDto.Builder()
                .title("Incepcja")
                .image("./img/movies/inception.jpg")
                .year(2010)
                .videoId("YoHD9XEInc0")
                .build();

        // when
        Long movieId = movieFacade.createMovie(movieDto);

        // then
        assertNotNull(movieId);
        Optional<MovieDto> foundMovie = movieFacade.findMovieDetailsById(movieId);
        assertTrue(foundMovie.isPresent());
        assertEquals("Incepcja", foundMovie.get().getTitle());
        assertEquals(2010, foundMovie.get().getYear());
        assertEquals("YoHD9XEInc0", foundMovie.get().getVideoId());
    }

    @Test
    void shouldFindAllMoviesOrderedByYearDesc() {
        // given
        MovieDto movie1 = new MovieDto.Builder().title("Starszy film").year(1990).build();
        MovieDto movie2 = new MovieDto.Builder().title("Nowszy film").year(2020).build();
        MovieDto movie3 = new MovieDto.Builder().title("Średni film").year(2005).build();

        movieFacade.createMovie(movie1);
        movieFacade.createMovie(movie2);
        movieFacade.createMovie(movie3);

        // when
        List<MovieDto> all = movieFacade.findAll();

        // then
        assertEquals(3, all.size());
        assertEquals("Nowszy film", all.get(0).getTitle());
        assertEquals("Średni film", all.get(1).getTitle());
        assertEquals("Starszy film", all.get(2).getTitle());
    }

    @Test
    void shouldUpdateExistingMovie() {
        // given
        MovieDto original = new MovieDto.Builder()
                .title("Matrix")
                .image("./img/movies/matrix.jpg")
                .year(1999)
                .videoId("vKQi3bBA1y8")
                .build();
        Long movieId = movieFacade.createMovie(original);

        MovieDto updatedData = new MovieDto.Builder()
                .title("Matrix Reaktywacja")
                .image("./img/movies/matrix2.jpg")
                .year(2003)
                .videoId("kYzz0ZsS4Uc")
                .build();

        // when (Zadanie 2: aktualizacja filmu)
        boolean updated = movieFacade.updateMovie(movieId, updatedData);

        // then
        assertTrue(updated);
        MovieDto movieAfterUpdate = movieFacade.show(movieId);
        assertEquals("Matrix Reaktywacja", movieAfterUpdate.getTitle());
        assertEquals(2003, movieAfterUpdate.getYear());
        assertEquals("kYzz0ZsS4Uc", movieAfterUpdate.getVideoId());
    }

    @Test
    void shouldReturnFalseWhenUpdatingNonExistingMovie() {
        // given
        Long nonExistingId = 99999L;
        MovieDto movieDto = new MovieDto.Builder()
                .title("Nieistniejący")
                .year(2025)
                .build();

        // when (Zadanie 2: film o podanym ID nie istnieje)
        boolean updated = movieFacade.updateMovie(nonExistingId, movieDto);

        // then
        assertFalse(updated);
    }

    @Test
    void shouldDeleteExistingMovie() {
        // given
        MovieDto movieDto = new MovieDto.Builder()
                .title("Do usunięcia")
                .year(2000)
                .build();
        Long movieId = movieFacade.createMovie(movieDto);

        // when
        boolean deleted = movieFacade.deleteMovie(movieId);

        // then
        assertTrue(deleted);
        assertTrue(movieFacade.findMovieDetailsById(movieId).isEmpty());
    }

    @Test
    void shouldReturnFalseWhenDeletingNonExistingMovie() {
        // when
        boolean deleted = movieFacade.deleteMovie(88888L);

        // then
        assertFalse(deleted);
    }

    @Test
    void shouldThrowMovieNotFoundExceptionOnShowWhenNotFound() {
        // when & then
        assertThrows(MovieNotFoundException.class, () -> movieFacade.show(77777L));
    }
}
