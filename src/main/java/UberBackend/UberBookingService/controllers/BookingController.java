package UberBackend.UberBookingService.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import UberBackend.UberBookingService.dto.CreateBookingDto;
import UberBackend.UberBookingService.dto.CreateBookingResponseDto;
import UberBackend.UberBookingService.dto.UpdateBookingRequestDto;
import UberBackend.UberBookingService.dto.UpdateBookingResponseDto;
import UberBackend.UberBookingService.services.BookingService;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {
	
	private final BookingService bookingService;
	
	 public BookingController(BookingService bookingService) {
		 this.bookingService=bookingService;
	 }

	@PostMapping
	public  ResponseEntity<CreateBookingResponseDto>  createBooking(@RequestBody CreateBookingDto createBookingDto) {
		
		return new ResponseEntity<>(bookingService.createBooking(createBookingDto),HttpStatus.CREATED);
	}
	
	@PostMapping("/{bookingId}")
	public ResponseEntity<UpdateBookingResponseDto> updateBooking(@RequestBody UpdateBookingRequestDto requestDto,@PathVariable Long bookingId){
		System.out.println(requestDto.getStatus()+" boking service"+requestDto.getDriverId());
		return new ResponseEntity<>(bookingService.updateBooking(requestDto,bookingId),HttpStatus.OK);
	}
}
