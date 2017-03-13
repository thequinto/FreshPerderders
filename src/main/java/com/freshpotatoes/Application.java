package com.freshpotatoes;

import com.freshpotatoes.domain.entity.Film;
import com.freshpotatoes.domain.entity.Genre;
import com.freshpotatoes.domain.repository.ArtistRepository;
import com.freshpotatoes.domain.repository.FilmRepository;
import com.freshpotatoes.domain.repository.GenreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;


@SpringBootApplication
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Autowired GenreRepository genreRepository;
    @Autowired FilmRepository filmRepository;
    @Autowired ArtistRepository artistRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

    @Bean
    public CommandLineRunner run() {
        return (args) -> {
            log.info("Genres found with findAll():");
            log.info("----------------------------");
            for (Genre genre : genreRepository.findAll()) {
                log.info(genre.toString());
            }

            log.info("Genres found with findOne(1L):");
            log.info("----------------------------");
            Genre genre = genreRepository.findOne(1L);
            log.info(genre.toString());
            log.info("");

            log.info("Films found with findTop10ByGenre(genre):");
            log.info("----------------------------");
            Sort sort = new Sort(Sort.Direction.ASC, "id");
            for (Film film : filmRepository.findTop10ByGenre(genre, sort)) {
                log.info(film.toString());
            }
            log.info("");

            log.info("Films found with findTop10ByGenre(genre2):");
            log.info("----------------------------");
            Genre anotherGenre = genreRepository.findOne(2L);
            for (Film film : filmRepository.findTop10ByGenre(anotherGenre, sort)) {
                log.info(film.toString());
            }
            log.info("");

//            log.info("Artists found with findTop10ByFilm(film):");
//            log.info("----------------------------");
//            Film film = filmRepository.findOne(1L);
//            Pageable limit = new PageRequest(0, 10);
//            for (Artist artist : artistRepository.findTop10ByFilms(Arrays.asList(film), limit)) {
//                log.info(artist.toString());
//            }
//            log.info("");
        };
    }
}
