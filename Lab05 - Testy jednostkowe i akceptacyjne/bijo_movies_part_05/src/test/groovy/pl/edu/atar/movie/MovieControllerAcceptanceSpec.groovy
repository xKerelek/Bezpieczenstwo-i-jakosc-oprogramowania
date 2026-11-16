package pl.edu.atar.movie

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.ResultActions
import pl.edu.atar.base.IntegrationSpec
import pl.edu.atar.movie.domain.MovieFacade
import pl.edu.atar.movie.domain.SampleMovies
import pl.edu.atar.movie.dto.MovieDto

import static org.hamcrest.Matchers.containsString
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

    def "should add a new movie via POST /api/movies"() {
        given: 'empty inventory'
        movieFacade.deleteAll()

        and: 'new movie json payload'
        String movieJson = """
        {
            "title": "Interstellar",
            "image": "./img/movies/interstellar.jpg",
            "year": 2014,
            "videoId": "zSWdZVtXT7E"
        }
        """

        when: 'I send POST request to /api/movies'
        ResultActions postMovie = mockMvc.perform(
                post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(movieJson)
        )

        then: 'status is 201 Created and Location header is present'
        postMovie.andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", containsString("/api/movies/")))
                .andExpect(jsonPath('$.title').value("Interstellar"))
                .andExpect(jsonPath('$.year').value(2014))
                .andExpect(jsonPath('$.videoId').value("zSWdZVtXT7E"))

        and: 'movie is saved in the database'
        List<MovieDto> allMovies = movieFacade.findAll()
        allMovies.size() == 1
        allMovies[0].title == "Interstellar"
        allMovies[0].year == 2014
        allMovies[0].videoId == "zSWdZVtXT7E"
    }

    def "should update existing movie via PUT /api/movies/{id}"() {
        given: 'inventory has Matrix'
        movieFacade.deleteAll()
        MovieDto savedMatrix = movieFacade.add(matrix)

        and: 'updated movie json payload'
        String updateJson = """
        {
            "title": "The Matrix Reloaded",
            "image": "./img/movies/matrix2.jpg",
            "year": 2003,
            "videoId": "kYzz0ZsS4Uc"
        }
        """

        when: 'I send PUT request to /api/movies/{id}'
        ResultActions updateAction = mockMvc.perform(
                put("/api/movies/${savedMatrix.movieId}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson)
        )

        then: 'status is 200 OK'
        updateAction.andExpect(status().isOk())

        and: 'movie is updated in the database'
        MovieDto updated = movieFacade.show(savedMatrix.movieId)
        updated.title == "The Matrix Reloaded"
        updated.year == 2003
        updated.videoId == "kYzz0ZsS4Uc"
    }

    def "should return 404 Not Found when getting non-existing movie"() {
        given: 'empty inventory'
        movieFacade.deleteAll()

        when: 'I send GET request for non-existing movie ID'
        ResultActions getAction = mockMvc.perform(get("/api/movies/9999"))

        then: 'status is 404 Not Found'
        getAction.andExpect(status().isNotFound())
    }

    def "should return 404 Not Found when updating non-existing movie"() {
        given: 'empty inventory'
        movieFacade.deleteAll()

        and: 'update payload'
        String updateJson = """
        {
            "title": "Non-existing Movie",
            "image": "./img/movies/none.jpg",
            "year": 2026,
            "videoId": "none"
        }
        """

        when: 'I send PUT request for non-existing movie ID'
        ResultActions updateAction = mockMvc.perform(
                put("/api/movies/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson)
        )

        then: 'status is 404 Not Found'
        updateAction.andExpect(status().isNotFound())
    }

    def "should delete movie via DELETE /api/movies/{id}"() {
        given: 'inventory has Matrix'
        movieFacade.deleteAll()
        MovieDto savedMatrix = movieFacade.add(matrix)

        when: 'I send DELETE request to /api/movies/{id}'
        ResultActions deleteAction = mockMvc.perform(delete("/api/movies/${savedMatrix.movieId}"))

        then: 'status is 200 OK'
        deleteAction.andExpect(status().isOk())

        and: 'movie is deleted from the database'
        mockMvc.perform(get("/api/movies/${savedMatrix.movieId}"))
                .andExpect(status().isNotFound())
    }

    def "should return 404 Not Found when deleting non-existing movie"() {
        given: 'empty inventory'
        movieFacade.deleteAll()

        when: 'I send DELETE request for non-existing movie ID'
        ResultActions deleteAction = mockMvc.perform(delete("/api/movies/9999"))

        then: 'status is 404 Not Found'
        deleteAction.andExpect(status().isNotFound())
    }
}
