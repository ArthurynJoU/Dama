package org.dama.service.impl.jpa;

import org.dama.entity.Score;
import org.dama.repository.ScoreRepository;
import org.dama.service.exception.ScoreException;
import org.dama.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Profile("server")
public class ScoreServiceJPA implements ScoreService {

    private final ScoreRepository scoreRepository;

    @Autowired
    public ScoreServiceJPA(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    @Override
    @Transactional
    public void addScore(Score score) throws ScoreException {
        try {
            scoreRepository.save(score);
        } catch (Exception ex) {
            throw new ScoreException("Cannot save score", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Score> getTopScores(String game, int limit) {
        return scoreRepository.findTop5ByGameOrderByPointsDesc(game);
    }

    @Override
    @Transactional
    public void reset() {
        scoreRepository.deleteAll();
    }
}
