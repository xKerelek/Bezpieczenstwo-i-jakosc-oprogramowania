package pl.edu.atar.domain.mapper;

import org.springframework.stereotype.Component;
import pl.edu.atar.domain.dto.MovieDetailDto;
import pl.edu.atar.domain.entity.Movie;

@Component
public class MovieDetailMapper implements Converter<MovieDetailDto, Movie> {

    @Override
    public MovieDetailDto convert(Movie from) {
        if (from == null) {
            return null;
        }

        MovieDetailDto movieDetailDto = new MovieDetailDto();
        movieDetailDto.setTitle(from.getTitle());
        movieDetailDto.setVideoId(from.getVideoId());

        return movieDetailDto;
    }
}
