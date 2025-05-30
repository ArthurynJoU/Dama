package org.dama.service.impl.rest;

import org.dama.service.RatingService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.dama.entity.Rating;

@Service
@Profile("client")
public class RatingServiceRestClient implements RatingService {
    private static final String URL = "http://localhost:8080/api/rating";

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void setRating(Rating rating) {
        restTemplate.postForObject(URL, rating, Void.class);
    }

    @Override
    public int getAverageRating(String game) {
        Double avg = restTemplate.getForObject(URL + "/" + game, Double.class);
        return avg != null ? avg.intValue() : 0;
    }


    @Override
    public int getRating(String game, String player) {
        return restTemplate.getForObject(URL + "/" + game + "/" + player, Integer.class);
    }

    @Override
    public void addRating(Rating rating) {
        // Implementácia pre jednoduché vloženie ratingu pomocou REST
        restTemplate.postForObject(URL, rating, Void.class);
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Reset not supported via REST client.");
    }
}
