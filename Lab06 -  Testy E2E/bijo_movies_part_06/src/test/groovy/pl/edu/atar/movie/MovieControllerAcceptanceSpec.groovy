package pl.edu.atar.movie

import org.hamcrest.Matchers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.ResultActions
import pl.edu.atar.base.IntegrationSpec
import pl.edu.atar.movie.domain.MovieFacade
import pl.edu.atar.movie.domain.SampleMovies
import pl.edu.atar.movie.dto.MovieDto

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class MovieControllerAcceptanceSpec extends IntegrationSpec implements SampleMovies {

    @Autowired
    MovieFacade movieFacade

    def "should get movies"() {
        given: 'inventory has Inception and Matrix'
        movieFacade.deleteAll()
        MovieDto savedSystem = movieFacade.add(inception)
        MovieDto savedTron = movieFacade.add(matrix)

        when: 'I go to /api/movies'
        ResultActions getMovies = mockMvc.perform(get("/api/movies"))

        then: 'I see all movies'
        getMovies.andExpect(status().isOk())
                .andExpect(content().json("""
            [
                {
                    "movieId": ${savedSystem.movieId},
                    "title": "${savedSystem.title}"
                },
                {
                    "movieId": ${savedTron.movieId},
                    "title": "${savedTron.title}"
                }
            ]"""))

        when: 'I go to /api/movies/{id}'
        ResultActions getMovie = mockMvc.perform(get("/api/movies/${savedSystem.movieId}"))

        then: 'I see details of that movie'
        getMovie.andExpect(status().isOk())
                .andExpect(content().json("""
            {
                "movieId": ${savedSystem.movieId},
                "title": "${savedSystem.title}"
            }"""))
    }

    def "should add a movie via POST and return 201 Created with Location header"() {
        given: 'a new movie payload'
        movieFacade.deleteAll()
        String movieJson = """
            {
                "title": "Inception",
                "image": "./img/movies/inception.jpg",
                "year": 2010,
                "videoId": "YoHD9XEInc0"
            }
        """

        when: 'I POST to /api/movies'
        ResultActions result = mockMvc.perform(
                post("/api/movies")
                        .contentType("application/json")
                        .content(movieJson)
        )

        then: 'response status is 201 Created'
        result.andExpect(status().isCreated())

        and: 'Location header points to the new resource'
        result.andExpect(header().exists("Location"))
                .andExpect(header().string("Location", Matchers.containsString("/api/movies/")))

        and: 'response body contains the saved movie with assigned id'
        result.andExpect(jsonPath('$.movieId').exists())
                .andExpect(jsonPath('$.title').value("Inception"))
    }

    def "should update an existing movie via PUT and return 200 OK"() {
        given: 'a movie exists in the system'
        movieFacade.deleteAll()
        MovieDto saved = movieFacade.add(inception)

        and: 'updated payload'
        String updatedJson = """
            {
                "title": "Inception Updated",
                "image": "./img/movies/inception_updated.jpg",
                "year": 2011,
                "videoId": "newVideoId"
            }
        """

        when: 'I PUT to /api/movies/{id}'
        ResultActions result = mockMvc.perform(
                put("/api/movies/${saved.movieId}")
                        .contentType("application/json")
                        .content(updatedJson)
        )

        then: 'response status is 200 OK'
        result.andExpect(status().isOk())

        and: 'the movie is updated in the system'
        movieFacade.show(saved.movieId).title == "Inception Updated"
        movieFacade.show(saved.movieId).year == 2011
    }

    def "should return 404 Not Found when updating a non-existing movie"() {
        given: 'a non-existing movie id'
        Long nonExistingId = 99999L
        String payload = """
            {
                "title": "Ghost Movie",
                "year": 2000,
                "image": "./img/ghost.jpg",
                "videoId": "abc123"
            }
        """

        when: 'I PUT to /api/movies/{nonExistingId}'
        ResultActions result = mockMvc.perform(
                put("/api/movies/${nonExistingId}")
                        .contentType("application/json")
                        .content(payload)
        )

        then: 'response status is 404 Not Found'
        result.andExpect(status().isNotFound())
    }

    def "should return 404 Not Found when fetching a non-existing movie"() {
        given: 'a non-existing movie id'
        Long nonExistingId = 99999L

        when: 'I GET /api/movies/{nonExistingId}'
        ResultActions result = mockMvc.perform(get("/api/movies/${nonExistingId}"))

        then: 'response status is 404 Not Found'
        result.andExpect(status().isNotFound())
    }

    def "should delete an existing movie via DELETE and return 200 OK"() {
        given: 'a movie exists in the system'
        movieFacade.deleteAll()
        MovieDto saved = movieFacade.add(matrix)

        when: 'I DELETE /api/movies/{id}'
        ResultActions result = mockMvc.perform(delete("/api/movies/${saved.movieId}"))

        then: 'response status is 200 OK'
        result.andExpect(status().isOk())

        and: 'movie list is now empty'
        movieFacade.findAll().isEmpty()
    }
}
