package org.dama;

import org.dama.entity.Score;
import org.dama.service.ScoreException;
import org.dama.service.ScoreService;
import org.dama.service.ScoreServiceJPA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreServiceJPATest {

    private ScoreService scoreService;

    @BeforeEach
    public void init() {
        scoreService = new ScoreServiceJPA();
        scoreService.reset();
    }

    @Test
    public void testAddScore() {
        Score score = new Score("Dama", "Víťaz", 12, LocalDateTime.now());
        scoreService.addScore(score);

        List<Score> skore = scoreService.getTopScores("Dama");
        assertFalse(skore.isEmpty());

        Score ulozeny = skore.get(0);
        assertEquals("Víťaz", ulozeny.getPlayer());
        assertEquals(12, ulozeny.getPoints());
    }

    @Test
    public void testResetScore() {
        scoreService.addScore(new Score("Dama", "A", 3, LocalDateTime.now()));
        scoreService.addScore(new Score("Dama", "B", 7, LocalDateTime.now()));

        assertTrue(scoreService.getTopScores("Dama").size() > 0);

        scoreService.reset();
        assertEquals(0, scoreService.getTopScores("Dama").size());
    }
}
