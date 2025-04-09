package org.dama.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.dama.entity.Score;
import org.dama.repository.ScoreRepository;
import java.util.List;

@Service
@Profile("server")
public class ScoreServiceJPA implements ScoreService {

    @Autowired
    private ScoreRepository scoreRepository;

    @Override
    public void addScore(Score score) {
        scoreRepository.save(score);
    }

    @Override
    public List<Score> getTopScores(String game) {
        return scoreRepository.findTop5ByGameOrderByPointsDesc(game);
    }

    @Override
    public void reset() {
        scoreRepository.deleteAll();
    }
}
