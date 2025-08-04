package UberBackend.UberBookingService.Consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

	
	@KafkaListener(topics = "sample-topic",groupId = "group-service-b")
	public void listen(String message) {
		System.out.println("kafka message from simple topic inside booking service "+message);
	}
}
