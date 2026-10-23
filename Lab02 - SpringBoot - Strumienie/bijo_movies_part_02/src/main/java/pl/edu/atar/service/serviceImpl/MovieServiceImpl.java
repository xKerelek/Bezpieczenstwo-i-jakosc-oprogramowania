package pl.edu.atar.service.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.edu.atar.domain.dto.CreateMovieDto;
import pl.edu.atar.domain.dto.MovieDetailDto;
import pl.edu.atar.domain.dto.MovieDto;
import pl.edu.atar.domain.entity.Movie;
import pl.edu.atar.domain.mapper.MovieDetailMapper;
import pl.edu.atar.domain.mapper.MovieListMapper;
import pl.edu.atar.domain.mapper.MovieMapper;
import pl.edu.atar.domain.repository.MovieRepository;
import pl.edu.atar.service.MovieService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MovieServiceImpl implements MovieService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieServiceImpl.class);

    private final MovieRepository movieRepository;
    private final MovieListMapper movieListMapper;
    private final MovieMapper movieMapper;
    private final MovieDetailMapper movieDetailMapper;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository,
                            MovieListMapper movieListMapper,
                            MovieMapper movieMapper,
                            MovieDetailMapper movieDetailMapper) {

        this.movieRepository = movieRepository;
        this.movieListMapper = movieListMapper;
        this.movieMapper = movieMapper;
        this.movieDetailMapper = movieDetailMapper;
    }

    @Override
    public List<MovieDto> findAll() {
        List<Movie> movies = movieRepository.findAll(Sort.by(Sort.Direction.DESC, "year"));
        return movieListMapper.convert(movies);
    }

    @Override
    public Long createMovie(CreateMovieDto createMovieDto) {

        Movie movie = movieMapper.convert(createMovieDto);
        Movie movieSaved = movieRepository.save(movie);

        return movieSaved.getMovieId();
    }

    @Override
    public boolean deleteMovie(Long movieId) {
        Optional<Movie> movieOptional = movieRepository.findById(movieId);

        if (movieOptional.isPresent()) {
            movieRepository.delete(movieOptional.get());
            return true;
        }
        return false;
    }

    @Override
    public MovieDetailDto movieDetailDto(Long movieId) {
        Movie movie = movieRepository.findById(movieId).orElse(null);

        if(movie == null) return null;

        return movieDetailMapper.convert(movie);
    }
}
