package co.ga.freshpotatoes.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import co.ga.freshpotatoes.domain.entity.ReviewResponse;

@Service
public class ReviewService {
    private static String URI = "http://credentials-api.generalassemb.ly/4576f55f-c427-4cfc-a11c-5bfe914ca6c1?";
    private static String FIELD1 = "films=";
    
    public List<ReviewResponse> getReviews(String filmIds, boolean useFilter) {
        RestTemplate template = new RestTemplate();
        ReviewResponse[] responses = template.getForObject(URI+FIELD1+filmIds, ReviewResponse[].class);
        List<ReviewResponse> responsesList = new ArrayList<>();
        for (ReviewResponse r : responses) {
            if (!useFilter || (r.getReviews().size() >= 5 && r.getAvgRating() >= 4.0))
                responsesList.add(r);
        }
        return responsesList;
    }
    
    public static void main(String[] args) {
        ReviewService s = new ReviewService();
        StringBuilder filmIds = new StringBuilder();
        for (int i = 1; i <= 100; i++) {
            filmIds.append(i);
            if (i < 100) filmIds.append(",");
        }
        List<ReviewResponse> r = s.getReviews(filmIds.toString(), true);
        for (ReviewResponse q : r) System.out.println(q);
    }
}
