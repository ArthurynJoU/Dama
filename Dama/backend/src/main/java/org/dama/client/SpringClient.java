package org.dama.client;

import org.dama.console.ConsoleUI;
import org.dama.service.CommentService;
import org.dama.service.impl.rest.CommentServiceRestClient;
import org.dama.service.RatingService;
import org.dama.service.impl.rest.RatingServiceRestClient;
import org.dama.service.ScoreService;
import org.dama.service.impl.rest.ScoreServiceRestClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.dama.client", "org.dama.core"})
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "org.dama.server.*"))
public class SpringClient implements CommandLineRunner {
    private final ConsoleUI consoleUI;

    public SpringClient(ConsoleUI consoleUI) {
        this.consoleUI = consoleUI;
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringClient.class)
                .profiles("client")
                .web(WebApplicationType.NONE)
                .run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        ScoreService scoreService = new ScoreServiceRestClient();
        CommentService commentService = new CommentServiceRestClient();
        RatingService ratingService = new RatingServiceRestClient();

        GameMenu menu = new GameMenu(scoreService, commentService, ratingService);
        menu.run();
    }
}
