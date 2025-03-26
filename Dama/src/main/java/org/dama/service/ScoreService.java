package org.dama.service;

import org.dama.entity.Score;
import java.util.List;

/**
 * Rozhranie pre ukladanie a čítanie výsledkov (score).
 */
public interface ScoreService {
    void addScore(Score score);
    List<Score> getTopScores(String game);
    void reset();
}
