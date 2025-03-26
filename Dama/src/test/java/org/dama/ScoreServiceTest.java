package org.dama.service;

import org.dama.entity.Score;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreServiceTest {

    private ScoreService scoreService;

    @BeforeEach
    void init() {
        scoreService = new ScoreServiceJDBC();
        scoreService.reset(); // очистим таблицу перед каждым тестом
    }

    @Test
    void testAddScore() {
        Score score = new Score("Dama", "TestUser", 5, new Date());
        scoreService.addScore(score);

        List<Score> list = scoreService.getTopScores("Dama");
        assertEquals(1, list.size());
        Score loaded = list.get(0);
        assertEquals("Dama", loaded.getGame());
        assertEquals("TestUser", loaded.getPlayer());
        assertEquals(5, loaded.getPoints());
    }

    @Test
    void testGetTopScores() {
        scoreService.addScore(new Score("Dama", "UserA", 10, new Date()));
        scoreService.addScore(new Score("Dama", "UserB", 20, new Date()));
        scoreService.addScore(new Score("Dama", "UserC", 15, new Date()));

        List<Score> list = scoreService.getTopScores("Dama");
        assertEquals(3, list.size());
        // сортировка по убыванию points => UserB, UserC, UserA
        assertEquals("UserB", list.get(0).getPlayer());
        assertEquals("UserC", list.get(1).getPlayer());
        assertEquals("UserA", list.get(2).getPlayer());
    }
}
