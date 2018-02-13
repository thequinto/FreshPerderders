package co.ga.freshpotatoes.domain.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import co.ga.freshpotatoes.domain.data.ReviewResponse;
import co.ga.freshpotatoes.domain.entity.Film;

@Service
public class ReviewService {
    private static final Logger log = LoggerFactory.getLogger(ReviewService.class);
    private static String URI = "http://credentials-api.generalassemb.ly/4576f55f-c427-4cfc-a11c-5bfe914ca6c1?";
    private static String FIELD1 = "films=";
    
    public List<Film> filterFilmsByReviews(List<Film> films, int minReviews, float minRating) {
        List<ReviewResponse> responses = getReviews(films, minReviews, minRating);
        Set<Long> filteredFilmIds = new HashSet<>();
        Map<Long, ReviewResponse> responsesMap = new HashMap<>();
        for (ReviewResponse r : responses) {
            filteredFilmIds.add(r.getFilmId());
            responsesMap.put(r.getFilmId(), r);
        }
        List<Film> filteredFilms = new LinkedList<>();
        for (Film f : films) {
            if (filteredFilmIds.contains(f.getId())) {
                filteredFilms.add(f);
                ReviewResponse response = responsesMap.get(f.getId());
                f.setAverageRating(response.getAvgRating());
                f.setReviews(response.getReviews().size());
            }
        }
        return filteredFilms;
    }
    
    private List<ReviewResponse> getReviews(List<Film> films, int minReviews, float minRating) {
        List<ReviewResponse> responsesList = new ArrayList<>();

        RestTemplate template = new RestTemplate();
        int BATCH_SIZE = 100;
        int min = 0;
        int max = BATCH_SIZE;
        do {
            StringBuilder s = new StringBuilder();
            for (int i = min; i < max; i++) {
                if (i == films.size()) break;
                s.append(films.get(i).getId());
                if (i < max - 1 && i < films.size() - 1) s.append(",");
            }
            log.info("going to reviews API: " + URI+FIELD1+s.toString());
            ReviewResponse[] responses = template.getForObject(URI+FIELD1+s.toString(), ReviewResponse[].class);
            for (ReviewResponse r : responses) {
                if (r.getReviews().size() >= minReviews && r.getAvgRating() >= minRating)
                    responsesList.add(r);
            }
            min += BATCH_SIZE;
            max += BATCH_SIZE;
        } while (max < films.size() + BATCH_SIZE);
        
        return responsesList;
    }
    
    private List<ReviewResponse> getReviewsByFilmIds(String filmIds) {
        List<ReviewResponse> responsesList = new ArrayList<>();
    
        RestTemplate template = new RestTemplate();
        String[] tokens = filmIds.split(",");
        int BATCH_SIZE = 100;
        int min = 0;
        int max = BATCH_SIZE;
        do {
            StringBuilder s = new StringBuilder();
            for (int i = min; i < max; i++) {
                if (i == tokens.length) break;
                s.append(tokens[i]);
                if (i < max - 1 && i < tokens.length - 1) s.append(",");
            }
            log.info("going to reviews API: " + URI+FIELD1+s.toString());
            ReviewResponse[] responses = template.getForObject(URI+FIELD1+s.toString(), ReviewResponse[].class);
            for (ReviewResponse r : responses) {
                if (r.getReviews().size() >= 5 && r.getAvgRating() >= 4.0)
                    responsesList.add(r);
            }
            min += BATCH_SIZE;
            max += BATCH_SIZE;
        } while (max < tokens.length + BATCH_SIZE);
        
        return responsesList;
    }

    public static void main(String[] args) {
        ReviewService s = new ReviewService();
        StringBuilder filmIds = new StringBuilder();
        int min = 7000;
        int max = 8000;
        for (int i = min; i <= max; i++) {
            filmIds.append(i);
            if (i < max - 1) filmIds.append(",");
        }
        List<ReviewResponse> r = s.getReviewsByFilmIds("7459,7535,7554,7566,7574,7579,7580,7611,7634,7635,7641,7674,7675,7684,7713,7719,7732,7740,7783,7807,7810,7821,7830,7846,7850,7912,7972,7990,8006,8013,8014,8015,8041,8048,8068,8070,8086,8088,8110,8128,8131,8133,8147,8168,8172,8193,8194,8227,8237,8263,8266,8308,8318,8320,8322,8334,8368,8370,8380,8389,8398,8430,8461,8467,8476,8496,8543,8548,8578,8614,8623,8639,8673,8743,8747,8748,8755,8756,8782,8826,8842,8843,8854,8904,8924,8937,8957,8986,8990,8993,9005,9027,9087,9120,9139,9191,9239,9276,9278,9291,9305,9308,9328,9332,9354,9404,9419,9421,9448,9456,9466,9508,9586,9589,9590,9616,9630,9642,9690,9712,9714,9715,9788,9790,9818,9822,9857,9869,9926,9948,9965,9969,9971,9982,9986,9994,9997,10040,10060,10084,10090,10099,10109,10129,10217,10247,10250,10273,10287");
        for (ReviewResponse q : r) System.out.println(q.getFilmId());
    }
}
