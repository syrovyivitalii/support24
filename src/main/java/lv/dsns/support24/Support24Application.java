package lv.dsns.support24;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Support24Application {

	public static void main(String[] args) {
		SpringApplication.run(Support24Application.class, args);
	}
}
