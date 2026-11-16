package pl.edu.atar.movie.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class MovieConfiguration {

    // Wersja używana w testach jednostkowych
    MovieFacade movieFacade() {
        return movieFacade(new InMemoryMovieRepository());
    }

    // Wersja produkcyjna rejestrowana w kontenerze Springa
    @Bean
    MovieFacade movieFacade(MovieRepository movieRepository) {
        MovieCreator movieCreator = new MovieCreator();
        return new MovieFacade(movieRepository, movieCreator);
    }
}
