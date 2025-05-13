package org.dama.service.impl.rest;

import org.dama.service.ScoreService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.dama.entity.Score;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Profile("client")
public class ScoreServiceRestClient implements ScoreService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String url = "http://localhost:8080/api/score";

    @Override
    public void addScore(Score score) {
        restTemplate.postForObject(url, score, Void.class);
    }

    @Override
    public List<Score> getTopScores(String game) {
        return Arrays.asList(Objects.requireNonNull(restTemplate.getForObject(url + "/" + game, Score[].class)));
    }

    @Override
    public void reset() {
        restTemplate.delete(url);
    }
}

