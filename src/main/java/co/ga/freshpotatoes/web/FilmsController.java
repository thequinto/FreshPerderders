package co.ga.freshpotatoes.web;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.ga.freshpotatoes.domain.data.ErrorResponse;
import co.ga.freshpotatoes.domain.data.RecommendedResponse;
import co.ga.freshpotatoes.domain.data.Response;
import co.ga.freshpotatoes.domain.entity.Film;
import co.ga.freshpotatoes.domain.entity.Genre;
import co.ga.freshpotatoes.domain.repository.FilmRepository;
import co.ga.freshpotatoes.domain.repository.GenreRepository;
import co.ga.freshpotatoes.domain.service.ReviewService;

@RestController
public class FilmsController {
  private static final int DEFAULT_OFFSET = 0;
  private static final int DEFAULT_LIMIT = 10;
  private static final Logger log = LoggerFactory.getLogger(FilmsController.class);
  @Autowired GenreRepository genreRepository;
  @Autowired FilmRepository filmRepository;
  @Autowired ReviewService reviewService;

  @RequestMapping(value="/films/{film_id}/recommendations", method=RequestMethod.GET)
  public Response recommendations(@PathVariable Long film_id,
          @RequestParam(value="offset", defaultValue = DEFAULT_OFFSET+"") String offset,
          @RequestParam(value="limit", defaultValue = DEFAULT_LIMIT+"") String limit)  {
      return getRecommendationsWithinYears(film_id, 7.5, limit, offset);
  }
  
  private Response getRecommendationsWithinYears(Long film_id, double numYears, String limit, String offset) {
      Film thisFilm = filmRepository.findOne(film_id);
      log.info("searching for parent film: " + thisFilm);
      if (thisFilm == null) return new ErrorResponse("parent film not found");
      Genre genre = thisFilm.getGenre();
      log.info("searching for films of genre: " + genre);
      List<Film> filmsByGenre = filmRepository.findByGenre(genre);
      // get from this genre films that have 5+ reviews averaging 4.0+
      List<Film> films = reviewService.filterFilmsByReviews(filmsByGenre, 5, 4.0f);
      StringBuilder s = new StringBuilder();
      for (Film f : films)
          s.append(f.getId() + " ");
      log.info("found these highly rated films in the same genre: " + s);
      // remove films outside 15 years
      double RANGE = numYears*365.25*24*60*60*1000;
      DateFormat df = new SimpleDateFormat("YYYY-MM-dd");
      try {
          Date thisDate = df.parse(thisFilm.getReleaseDate());
          Set<Film> unrelatedFilms = new HashSet<>();
          for (Film f : films) {
              try {
                  Date releaseDate = df.parse(f.getReleaseDate());
                  if (Math.abs(thisDate.getTime() - releaseDate.getTime()) > RANGE) {
                      log.info("this film is outside the date range: " + f);
                      unrelatedFilms.add(f);
                  }
              } catch (ParseException e) {
                  log.warn("parsed and ignored a bad release date for film: " + f.getId());
                  e.printStackTrace();
              }
          }
          films.removeAll(unrelatedFilms);
          log.info("removed films outside of +-" + RANGE + " ms");
      } catch (ParseException e) {
          log.warn("parsed a bad release date for parent film: " + thisFilm.getId());
          e.printStackTrace();
      }
      // remove the originally requested film
      films.remove(thisFilm);
      // finally sort by film ID
      films.sort(new java.util.Comparator<Film>() {
        @Override
        public int compare(Film o1, Film o2) {
            return (int)(o1.getId() - o2.getId());
        }
      });
      int intOffset = DEFAULT_OFFSET;
      int intLimit = DEFAULT_LIMIT;
      try {
          intOffset = Integer.parseInt(offset);
          if (intOffset < 0) {
              return new ErrorResponse("negative offset");
          }
          if (intOffset >= films.size()) {
              log.warn("offset out of bounds, resetting to default: " + DEFAULT_OFFSET);
              intOffset = DEFAULT_OFFSET;
          }
      } catch (NumberFormatException e) {
          log.warn("NFE when parsing requested offset");
          return new ErrorResponse("exceptional number format for offset parameter");
      }
      try {
          intLimit = Integer.parseInt(limit);
      } catch (NumberFormatException e) {
          log.warn("NFE when parsing requested limit");
          return new ErrorResponse("exceptional number format for limit parameter");
      }
      Set<Film> recFilms = new java.util.LinkedHashSet<>(films.subList(intOffset, intOffset+intLimit > films.size() ? films.size() : intOffset+intLimit));
      return new RecommendedResponse(recFilms, limit, offset);
  }
}
