package co.ga.freshpotatoes.domain.repository;

import co.ga.freshpotatoes.domain.entity.Genre;
import org.springframework.data.repository.CrudRepository;

public interface GenreRepository extends CrudRepository<Genre, Long> {
  Genre findOneByName(String name);
}
