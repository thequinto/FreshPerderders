package co.ga.freshpotatoes.domain.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "artists")
public class Artist {
    private long id;
    private String name;
    private String birthday;
    private String deathday;
    private int gender;
    private String placeOfBirth;
    private Set<Film> films;

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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getDeathday() {
        return deathday;
    }

    public void setDeathday(String deathday) {
        this.deathday = deathday;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    @Column(name = "place_of_birth")
    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "artist_films",
            joinColumns = @JoinColumn(name = "artist_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "film_id", referencedColumnName = "id"))
    public Set<Film> getFilms() {
        return films;
    }

    public void setFilms(Set<Film> films) {
        this.films = films;
    }

    @Override
    public String toString() {
        String result = String.format(
                "Artist[" +
                        "id=%d, " +
                        "name='%s', " +
                        "birthday='%s', " +
                        "deathday='%s', " +
                        "gender='%d', " +
                        "place_of_birth='%s'" +
                        "]",
                id,
                name,
                birthday,
                deathday,
                gender,
                placeOfBirth);

        if (films != null) {
            for (Film film : films) {
                result += String.format("Film[id='%d']", film.getId());
            }
        }
        return result;
    }
}
