package UberBackend.UberBookingService.dto;

import java.util.Optional;

import UberBackend.UberProject_EntityService.models.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookingRequestDto {

	private String status;
	private Optional<Long> driverId;

}
