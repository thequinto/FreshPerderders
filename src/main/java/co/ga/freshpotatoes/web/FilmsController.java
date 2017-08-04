package co.ga.freshpotatoes.web;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import co.ga.freshpotatoes.domain.entity.Film;
import co.ga.freshpotatoes.domain.entity.Genre;
import co.ga.freshpotatoes.domain.repository.FilmRepository;
import co.ga.freshpotatoes.domain.repository.GenreRepository;

@RestController
public class FilmsController {
  @Autowired GenreRepository genreRepository;
  @Autowired FilmRepository filmRepository;

  private static final String template = "id=%s, offset=%s, limit=%s\n";

  @RequestMapping(value="/films/{film_id}/recommendations", method=RequestMethod.GET)
  public Set<Film> recommendations(@PathVariable Long film_id,
    @RequestParam(value="offset", defaultValue = "0") String offset,
    @RequestParam(value="limit", defaultValue = "10") String limit) {
      Film film = filmRepository.findOne(film_id);
      Genre genre = genreRepository.findOne(film.getGenre().getId());

      return genre.getFilms();
  }
}
