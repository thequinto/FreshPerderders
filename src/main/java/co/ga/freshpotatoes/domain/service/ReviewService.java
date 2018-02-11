package co.ga.freshpotatoes.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import co.ga.freshpotatoes.domain.entity.ReviewResponse;

@Service
public class ReviewService {
    private static String URI = "http://credentials-api.generalassemb.ly/4576f55f-c427-4cfc-a11c-5bfe914ca6c1?";
    private static String FIELD1 = "films=";
    
    public void getReviews(String filmIds) {
        RestTemplate template = new RestTemplate();
        ReviewResponse[] responses = template.getForObject(URI+FIELD1+filmIds, ReviewResponse[].class);
        for (ReviewResponse r : responses)
            System.out.println(r);
    }
    
    public static void main(String[] args) {
        ReviewService s = new ReviewService();
        s.getReviews("8,8");
    }
}
