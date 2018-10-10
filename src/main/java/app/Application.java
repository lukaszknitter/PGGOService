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

	private static boolean isToLoad = true;
	private final DataLoader dataLoader;

	public static void main(String[] args) {
		if (args.length > 0 && "nofill".equals(args[0])) {
			isToLoad = false;
		}
		SpringApplication.run(Application.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void fillDatabase() {
		if (isToLoad) {
			long startTime = System.currentTimeMillis();
			System.out.println("Filling up database...");
			dataLoader.loadBuildings();
			System.out.println("Database filled up in " + (double) (System.currentTimeMillis() - startTime) / 1000 + "s");
		}
	}
}
