package pl.edu.atar.movie.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class MovieDto implements Serializable {

    private Long movieId;
    private String title;
    private String image;
    private Integer year;
    private String videoId;
}
