package UberBackend.UberBookingService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableJpaAuditing
@EnableKafka
@EntityScan(basePackages = {
	    "UberBackend.UberBookingService.models",
	    "UberBackend.UberProject_EntityService.models"  // Add this line
	})
@EnableEurekaClient
public class UberBookingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UberBookingServiceApplication.class, args);
	}

	  @Bean
	    public RestTemplate restTemplate() {
	        return new RestTemplate();
	    }
}
