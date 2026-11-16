package pl.edu.atar.movie.domain

import groovy.transform.CompileStatic
import pl.edu.atar.movie.dto.MovieDto

@CompileStatic
trait SampleMovies {

    MovieDto matrix = MovieDto.builder()
            .movieId(200L)
            .title("The Matrix")
            .image("./img/movies/matrix.jpg")
            .year(1999)
            .videoId("vKQi3bBA1y8")
            .build()

    MovieDto inception = MovieDto.builder()
            .movieId(201L)
            .title("Inception")
            .image("./img/movies/inception.jpg")
            .year(2010)
            .videoId("YoHD9XEInc0")
            .build()
}
