package app;

import app.dataloader.DataLoader;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

@SpringBootApplication
@EnableJpaRepositories
@Service
@AllArgsConstructor
public class Application {

    private final DataLoader dataLoader;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void fillDatabase() {
        System.out.println("Filling up database...");
        dataLoader.loadBuildings();
        System.out.println("Database filled up!");
    }
}
