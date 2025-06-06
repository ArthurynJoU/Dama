package org.dama.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.dama.entity.Score;
import org.dama.service.ScoreService;
import java.util.List;

@RestController
@RequestMapping("/api/score")
public class ScoreController {

    private final ScoreService scoreService;

    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @PostMapping
    public void addScore(@RequestBody Score score) {
        scoreService.addScore(score);
    }

    @GetMapping("/{game}")
    public List<Score> getTopScores(
        @PathVariable String game,
        @RequestParam(defaultValue = "10") int limit
    ) {
                return scoreService.getTopScores(game, limit);
            }

    @DeleteMapping
    public void reset() {
        scoreService.reset();
    }
}

