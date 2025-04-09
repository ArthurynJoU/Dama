package org.dama.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.dama.entity.Score;
import java.util.Arrays;
import java.util.List;

@Service
@Profile("client")
public class ScoreServiceRestClient implements ScoreService {
    private static final String URL = "http://localhost:8080/api/score";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void addScore(Score score) {
        restTemplate.postForObject(URL, score, Void.class);
    }

    @Override
    public List<Score> getTopScores(String game) {
        Score[] scores = restTemplate.getForObject(URL + "/" + game, Score[].class);
        return scores != null ? Arrays.asList(scores) : List.of();
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Reset not supported via REST client.");
    }
}
