package UberBackend.UberBookingService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingConfirmedEvent {

	    private Long bookingId;
	    private Long passengerId;
	    private Long driverId;
	    private String driverName;
	    private String status;
}
