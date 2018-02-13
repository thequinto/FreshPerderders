package co.ga.freshpotatoes.domain.data;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import co.ga.freshpotatoes.domain.entity.Review;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewResponse extends Response {
    @JsonProperty(value = "film_id")
    private long filmId;
    private List<Review> reviews;

    public float getAvgRating() {
        float sum = 0;
        for (Review r : reviews) {
            sum += r.getRating();
        }
        DecimalFormat df = new DecimalFormat("#.#");
        return Float.parseFloat(df.format(sum / reviews.size()));
    }
    
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
        s.append("],\navgRating:").append(getAvgRating())
        .append("}");
        return s.toString();
    }
}
