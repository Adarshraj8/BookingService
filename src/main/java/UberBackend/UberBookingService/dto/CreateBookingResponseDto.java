package UberBackend.UberBookingService.dto;

import java.util.Optional;

import UberBackend.UberProject_EntityService.models.Driver;
import UberBackend.UberProject_EntityService.models.ExactLocation;
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
public class CreateBookingResponseDto {

	private Long BookingId;
	
	private String bookingStatus;
	
	private Optional<Driver> driver;
	
	
}
