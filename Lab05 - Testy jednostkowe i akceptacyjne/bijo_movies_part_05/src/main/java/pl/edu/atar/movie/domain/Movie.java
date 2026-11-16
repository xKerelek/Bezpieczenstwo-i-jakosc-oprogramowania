package pl.edu.atar.movie.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.edu.atar.movie.dto.MovieDto;

import javax.persistence.*;
import java.io.Serializable;

@Builder
@Entity
@Table(name = "movies")
@AllArgsConstructor
@NoArgsConstructor
@Getter
class Movie implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieId;
    private String title;
    private String image;
    private Integer publicationYear;
    private String videoId;

    MovieDto dto() {
        return MovieDto.builder()
                .movieId(movieId)
                .title(title)
                .image(image)
                .year(publicationYear)
                .videoId(videoId)
                .build();
    }
}
