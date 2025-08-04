package UberBackend.UberBookingService.services;

import UberBackend.UberBookingService.dto.CreateBookingDto;
import UberBackend.UberBookingService.dto.CreateBookingResponseDto;
import UberBackend.UberBookingService.dto.UpdateBookingRequestDto;
import UberBackend.UberBookingService.dto.UpdateBookingResponseDto;

public interface BookingService {

	CreateBookingResponseDto createBooking(CreateBookingDto bookingDetails);
	
	UpdateBookingResponseDto updateBooking(UpdateBookingRequestDto bookingRequestDto,Long bookingId);
}
