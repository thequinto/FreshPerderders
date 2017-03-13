package com.freshpotatoes.domain.service;

import com.freshpotatoes.domain.entity.Artist;
import com.freshpotatoes.domain.entity.Film;
import com.freshpotatoes.domain.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistService {
    @Autowired ArtistRepository artistRepository;

    public List<Artist> findTop10ByFilms(List<Film> films, Sort sort) {
        return artistRepository.findTop10ByFilms(films, sort);
    }
}
