package co.ga.freshpotatoes.web;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import co.ga.freshpotatoes.domain.entity.Film;
import co.ga.freshpotatoes.domain.entity.Genre;
import co.ga.freshpotatoes.domain.repository.FilmRepository;
import co.ga.freshpotatoes.domain.repository.GenreRepository;
import co.ga.freshpotatoes.domain.service.ReviewService;

@RestController
public class FilmsController {
  private static final Logger log = LoggerFactory.getLogger(FilmsController.class);
  @Autowired GenreRepository genreRepository;
  @Autowired FilmRepository filmRepository;
  @Autowired ReviewService reviewService;

  private static final String template = "id=%s, offset=%s, limit=%s\n";

  @RequestMapping(value="/films/{film_id}/recommendations", method=RequestMethod.GET)
  public Set<Film> recommendations(@PathVariable Long film_id) throws ParseException {
      return getRecommendationsWithinYears(film_id, 7.5);
  }
  
  private Set<Film> getRecommendationsWithinYears(Long film_id, double numYears) throws ParseException {
      Film thisFilm = filmRepository.findOne(film_id);
      log.info("searching for genre of: " + film_id);
      Genre genre = thisFilm.getGenre();
      log.info("searching for films of genre: " + genre);
      List<Film> filmsByGenre = filmRepository.findByGenre(genre);
      // get from this genre films that have 5+ reviews averaging 4.0+
      List<Film> films = reviewService.filterFilmsByReviews(filmsByGenre, 5, 4.0f);
      StringBuilder s = new StringBuilder();
      for (Film f : films)
          s.append(f.getId() + " ");
      log.info("found in these highly rated films in the same genre: " + s);
      // remove films outside 15 years
      double RANGE = numYears*365.25*24*60*60*1000;
      DateFormat df = new SimpleDateFormat("YYYY-MM-dd");
      Date thisDate = df.parse(thisFilm.getReleaseDate());
      for (Film f : films) {
          Date releaseDate = df.parse(f.getReleaseDate());
          if (Math.abs(thisDate.getTime() - releaseDate.getTime()) > RANGE) {
              films.remove(f);
          }
      }
      log.info("removed films outside of +-" + RANGE + " ms");
      // remove the originally requested film
      films.remove(thisFilm);
      // finally sort by film ID
      films.sort(new java.util.Comparator<Film>() {
        @Override
        public int compare(Film o1, Film o2) {
            return (int)(o1.getId() - o2.getId());
        }
      });
      return new java.util.LinkedHashSet<Film>(films);
  }
}
