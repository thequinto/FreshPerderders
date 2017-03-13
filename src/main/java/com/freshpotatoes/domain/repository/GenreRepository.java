package com.freshpotatoes.domain.repository;

import com.freshpotatoes.domain.entity.Genre;
import org.springframework.data.repository.CrudRepository;

public interface GenreRepository extends CrudRepository<Genre, Long> {
}
