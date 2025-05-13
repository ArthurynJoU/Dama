package org.dama.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "org.dama.server",
        "org.dama.service",
        "org.dama.repository",
        "org.dama.entity",
        "org.dama.core"
})
@EntityScan("org.dama.entity")
@EnableJpaRepositories("org.dama.repository")
public class GameStudioServer {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(GameStudioServer.class);
        app.setAdditionalProfiles("server");
        app.run(args);
    }
}
