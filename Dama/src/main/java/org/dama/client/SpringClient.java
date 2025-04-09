package org.dama.client;

import org.dama.console.ConsoleUI;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication(scanBasePackages = {"org.dama.client", "org.dama.core"})
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "org.dama.server.*"))
public class SpringClient implements CommandLineRunner {
    private final ConsoleUI consoleUI;

    public SpringClient(ConsoleUI consoleUI) {
        this.consoleUI = consoleUI;
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringClient.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        consoleUI.play();
    }
}

