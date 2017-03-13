package com.freshpotatoes.domain.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "genres")
public class Genre {
    private long id;
    private String name;
    private Set<Film> films;

    public Genre() {}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL)
    public Set<Film> getFilms() {
        return films;
    }

    public void setFilms(Set<Film> films) {
        this.films = films;
    }

    @Override
    public String toString() {
        return String.format("Genre[id=%d, name='%s']", id, name);
    }
}
