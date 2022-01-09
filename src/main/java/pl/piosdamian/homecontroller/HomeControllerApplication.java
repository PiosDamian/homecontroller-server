package pl.piosdamian.homecontroller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HomeControllerApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomeControllerApplication.class, args);
	}
}
