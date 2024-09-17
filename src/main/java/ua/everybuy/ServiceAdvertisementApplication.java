package ua.everybuy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ServiceAdvertisementApplication {
	public static void main(String[] args) {
		SpringApplication.run(ServiceAdvertisementApplication.class, args);
	}

}
