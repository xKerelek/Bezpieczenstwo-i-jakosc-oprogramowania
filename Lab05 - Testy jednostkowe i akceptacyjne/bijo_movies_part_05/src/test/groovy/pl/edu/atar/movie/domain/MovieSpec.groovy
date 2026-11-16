package pl.edu.atar.movie.domain

import pl.edu.atar.movie.dto.MovieDto
import pl.edu.atar.movie.dto.MovieNotFoundException
import spock.lang.Specification

class MovieSpec extends Specification implements SampleMovies {

    // Spock Framework Reference Documentation:
    // https://spockframework.org/spock/docs/2.5/all_in_one.html

    MovieFacade facade

    def setup() {
        facade = new MovieConfiguration().movieFacade()
    }

    def "should show a movie"() {
        given: "movie is in the system"
        facade.add(inception)

        expect: "system return the movie"
        facade.show(inception.movieId) == inception
    }

    def "should add a movie"() {
        when: "movie is added to the system"
        MovieDto added = facade.add(matrix)

        then: "system returns added movie"
        added == matrix

        and: "movie can be retrieved from the system"
        facade.show(matrix.movieId) == matrix
    }

    def "should delete a movie"() {
        given: "movie is in the system"
        facade.add(matrix)

        when: "movie is deleted"
        boolean deleted = facade.delete(matrix.movieId)

        then: "deletion returns true"
        deleted

        when: "trying to show the deleted movie"
        facade.show(matrix.movieId)

        then: "MovieNotFoundException is thrown"
        thrown(MovieNotFoundException)
    }

    def "should update a movie"() {
        given: "movie is in the system"
        facade.add(matrix)

        and: "updated movie details"
        MovieDto updatedMatrix = MovieDto.builder()
                .title("The Matrix Reloaded")
                .image("./img/movies/matrix2.jpg")
                .year(2003)
                .videoId("kYzz0ZsS4Uc")
                .build()

        when: "movie is updated"
        MovieDto result = facade.update(matrix.movieId, updatedMatrix)

        then: "system returns updated movie with the same id"
        result.movieId == matrix.movieId
        result.title == "The Matrix Reloaded"
        result.year == 2003
        result.videoId == "kYzz0ZsS4Uc"

        and: "retrieving movie from system returns updated data"
        facade.show(matrix.movieId).title == "The Matrix Reloaded"
    }

    def "should list films"() {
        given: "movies are in the system"
        facade.add(matrix)    // rok 1999
        facade.add(inception) // rok 2010

        when: "listing all movies"
        List<MovieDto> movies = facade.findAll()

        then: "movies are returned ordered by publication year descending"
        movies.size() == 2
        movies[0] == inception
        movies[1] == matrix
    }

    def "should throw exception when asked for a movie that's not in the system"() {
        when: "asking for a movie that is not in the system"
        facade.show(9999L)

        then: "MovieNotFoundException is thrown"
        thrown(MovieNotFoundException)
    }

}
