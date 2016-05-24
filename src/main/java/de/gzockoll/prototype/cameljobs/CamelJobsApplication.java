package de.gzockoll.prototype.cameljobs;

import com.google.common.eventbus.EventBus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CamelJobsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CamelJobsApplication.class, args);
	}

}
