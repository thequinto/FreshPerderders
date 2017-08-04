package co.ga.freshpotatoes;

import co.ga.freshpotatoes.domain.entity.Film;
import co.ga.freshpotatoes.domain.entity.Genre;
import co.ga.freshpotatoes.domain.repository.ArtistRepository;
import co.ga.freshpotatoes.domain.repository.FilmRepository;
import co.ga.freshpotatoes.domain.repository.GenreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;

import java.util.List;

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
      log.info("Film counts by genre:");
      log.info("----------------------------");
      for (Genre genre : genreRepository.findAll()) {

        // Can't use genre.getFilms().size() without a session
        log.info(String.format("%d films in genre '%s'",
                               filmRepository.findByGenre(genre).size(),
                               genre.getName()));
        log.info("----------------------------");
      }
    };
  }
}
