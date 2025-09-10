package UberBackend.UberBookingService.Consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import UberBackend.UberBookingService.dto.BookingUpdateEvent;
import UberBackend.UberBookingService.services.BookingService;

@Service
public class KafkaConsumerService {

    private final ObjectMapper objectMapper;
    private final BookingService bookingService;

    public KafkaConsumerService(ObjectMapper objectMapper, BookingService bookingService) {
        this.objectMapper = objectMapper;
        this.bookingService = bookingService;
    }

    @KafkaListener(topics = "booking-update-topic", groupId = "group-service-b")
    public void listen(String message) {
        try {
            BookingUpdateEvent event = objectMapper.readValue(message, BookingUpdateEvent.class);

            System.out.println("📩 Booking Service received update: " + event);

            // 🔥 Call the same service method you already use in controller
            bookingService.updateBooking(event.getRequestDto(), event.getBookingId());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
