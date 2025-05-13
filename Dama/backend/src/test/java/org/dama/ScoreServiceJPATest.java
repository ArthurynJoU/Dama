package org.dama;

import org.dama.entity.Score;
import org.dama.repository.ScoreRepository;
import org.dama.service.impl.jpa.ScoreServiceJPA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScoreServiceJPATest {

    @Mock
    private ScoreRepository scoreRepository;

    @InjectMocks
    private ScoreServiceJPA scoreService;

    private Score sampleScore;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleScore = new Score("Dama", "Víťaz", 12, LocalDateTime.now());
    }

    @Test
    void testAddScore() {
        when(scoreRepository.findTop5ByGameOrderByPointsDesc("Dama"))
                .thenReturn(List.of(sampleScore));
        scoreService.addScore(sampleScore);
        verify(scoreRepository, times(1)).save(sampleScore);

        List<Score> scores = scoreService.getTopScores("Dama", limit);
        assertFalse(scores.isEmpty());
        Score saved = scores.get(0);
        assertEquals("Víťaz", saved.getPlayer());
        assertEquals(12, saved.getPoints());
    }

    @Test
    void testResetScore() {
        when(scoreRepository.findTop5ByGameOrderByPointsDesc("Dama"))
                .thenReturn(List.of(
                        new Score("Dama", "A", 3, LocalDateTime.now()),
                        new Score("Dama", "B", 7, LocalDateTime.now())
                ))
                .thenReturn(List.of());
        scoreService.addScore(new Score("Dama", "A", 3, LocalDateTime.now()));
        scoreService.addScore(new Score("Dama", "B", 7, LocalDateTime.now()));
        verify(scoreRepository, times(2)).save(any(Score.class));
        assertTrue(scoreService.getTopScores("Dama", limit).size() > 0);

        scoreService.reset();
        verify(scoreRepository, times(1)).deleteAll();
        assertEquals(0, scoreService.getTopScores("Dama", limit).size());
    }
}
