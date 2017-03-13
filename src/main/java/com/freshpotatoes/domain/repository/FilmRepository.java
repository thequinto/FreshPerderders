package com.freshpotatoes.domain.repository;

import com.freshpotatoes.domain.entity.Film;
import com.freshpotatoes.domain.entity.Genre;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FilmRepository extends CrudRepository<Film, Long> {
    List<Film> findTop10ByGenre(Genre genre, Sort sort);
}

