package org.dama.client;

import org.dama.console.ConsoleUI;
import org.dama.core.Board;
import org.dama.core.Game;
import org.dama.service.*;
import org.springframework.context.annotation.*;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GameConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ScoreService scoreService() {
        return new ScoreServiceRestClient();
    }

    @Bean
    public CommentService commentService() {
        return new CommentServiceRestClient();
    }

    @Bean
    public RatingService ratingService() {
        return new RatingServiceRestClient();
    }

    @Bean
    public Board board() {
        return new Board();
    }

    @Bean
    public Game game(Board board, ScoreService scoreService, CommentService commentService, RatingService ratingService) {
        return new Game("Biely", "Čierny", false, board);
    }

    @Bean
    public ConsoleUI consoleUI(Game game) {
        return new ConsoleUI(game);
    }
}
