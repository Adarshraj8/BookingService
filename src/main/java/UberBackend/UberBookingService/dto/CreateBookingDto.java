package UberBackend.UberBookingService.dto;

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
public class CreateBookingDto {

	private Long passengerId;
	
	private ExactLocation startLocation;
	
	private ExactLocation endLocation;
}
