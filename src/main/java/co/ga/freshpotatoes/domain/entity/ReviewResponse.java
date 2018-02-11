package co.ga.freshpotatoes.domain.entity;

import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewResponse {
    @JsonProperty(value = "film_id")
    private long filmId;
    private List<Review> reviews;

    public ReviewResponse() {}

    public long getFilmId() {
        return filmId;
    }

    public void setFilmId(long filmId) {
        this.filmId = filmId;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
    
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("{filmID:").append(filmId)
        .append(", reviews:[");
        Iterator<Review> i = reviews.iterator();
        while (i.hasNext()) {
            s.append(i.next());
            if (i.hasNext())
                s.append(",\n\t");
        }
        s.append("]}");
        return s.toString();
    }
}
