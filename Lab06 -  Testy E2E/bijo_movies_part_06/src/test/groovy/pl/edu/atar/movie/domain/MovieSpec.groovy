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
        MovieDto saved = facade.add(inception)

        then: "system returns the saved movie with an assigned id"
        saved.title == inception.title
        saved.year == inception.year
        saved.image == inception.image
        saved.videoId == inception.videoId
        saved.movieId != null
    }

    def "should delete a movie"() {
        given: "movie is in the system"
        facade.add(inception)
        MovieDto saved = facade.add(matrix)

        when: "movie is deleted"
        boolean result = facade.delete(saved.movieId)

        then: "system confirms deletion"
        result

        and: "movie is no longer in the system"
        facade.findAll().size() == 1
    }

    def "should update a movie"() {
        given: "movie is in the system"
        MovieDto saved = facade.add(inception)

        and: "updated data"
        MovieDto updatedData = MovieDto.builder()
                .title("Inception Updated")
                .year(2011)
                .image("./img/movies/inception_updated.jpg")
                .videoId("newVideoId")
                .build()

        when: "movie is updated"
        MovieDto result = facade.update(saved.movieId, updatedData)

        then: "system returns the updated movie"
        result.movieId == saved.movieId
        result.title == "Inception Updated"
        result.year == 2011
    }

    def "should list films"() {
        given: "two movies are in the system"
        facade.add(inception)
        facade.add(matrix)

        when: "all movies are requested"
        List<MovieDto> all = facade.findAll()

        then: "system returns all movies"
        all.size() == 2
        all.any { it.title == inception.title }
        all.any { it.title == matrix.title }
    }

    def "should throw exception when asked for a movie that's not in the system"() {
        when: "non-existing movie is requested"
        facade.show(9999L)

        then: "system throws MovieNotFoundException"
        thrown(MovieNotFoundException)
    }

}
