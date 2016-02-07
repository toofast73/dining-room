package ru.live.toofast;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class DiningHallSimulatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiningHallSimulatorApplication.class, args);
	}


}
