package UberBackend.UberBookingService.services;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import UberBackend.UberBookingService.apis.LocationServiceApi;
import UberBackend.UberBookingService.apis.UberSocketApi;
import UberBackend.UberBookingService.dto.BookingConfirmedEvent;
import UberBackend.UberBookingService.dto.CreateBookingDto;
import UberBackend.UberBookingService.dto.CreateBookingResponseDto;
import UberBackend.UberBookingService.repositories.BookingRepository;
import UberBackend.UberBookingService.repositories.DriverRepository;
import UberBackend.UberBookingService.repositories.PassengerRepository;
import UberBackend.UberProject_EntityService.models.Booking;
import UberBackend.UberProject_EntityService.models.BookingStatus;
import UberBackend.UberProject_EntityService.models.Driver;
import UberBackend.UberProject_EntityService.models.ExactLocation;
import UberBackend.UberProject_EntityService.models.Passenger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import UberBackend.UberBookingService.dto.DriverLocationDto;
import UberBackend.UberBookingService.dto.NearbyDriversRequestDto;
import UberBackend.UberBookingService.dto.RideRequestDto;
import UberBackend.UberBookingService.dto.UpdateBookingRequestDto;
import UberBackend.UberBookingService.dto.UpdateBookingResponseDto;



@Service
public class BookingServiceImpl implements BookingService{

	private PassengerRepository passengerRepository;
	private BookingRepository bookingRepository;
	private final RestTemplate restTemplate;
	private final LocationServiceApi locationServiceApi;
	private DriverRepository driverRepository;
	private final UberSocketApi uberSocketApi;
//	private static final String LOCATION_SERVICE="http://localhost:1003";
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	public BookingServiceImpl(PassengerRepository passengerRepository,
			BookingRepository bookingRepository,
			RestTemplate restTemplate,
			LocationServiceApi locationServiceApi,
			DriverRepository driverRepository,
			UberSocketApi uberSocketApi) {
		this.passengerRepository=passengerRepository;
		this.bookingRepository=bookingRepository;
		this.restTemplate = restTemplate;
		this.locationServiceApi=locationServiceApi;
		this.driverRepository=driverRepository;
		this.uberSocketApi=uberSocketApi;
	}
	@Override
	public CreateBookingResponseDto createBooking(CreateBookingDto bookingDetails) {
		
		  Optional<Passenger> passenger = passengerRepository.findById(bookingDetails.getPassengerId());

         
         
          Booking booking =   Booking.builder()
		   .bookingStatus(BookingStatus.ASSIGNING_DRIVER)
		   .startLocation(bookingDetails.getStartLocation())
		   .endLocation(bookingDetails.getEndLocation())
		   .passenger(passenger.get())
		   .build();
         Booking newBooking = bookingRepository.save(booking);
         
          NearbyDriversRequestDto request = NearbyDriversRequestDto.builder()
         .latitude(bookingDetails.getStartLocation().getLatitude())
         .longitude(bookingDetails.getEndLocation().getLongitude())
         .build();
          
          processNearbyDriverAsync(request,bookingDetails.getPassengerId(),bookingDetails,newBooking.getId());
//         //make api call to LocationService to fetch nearby drivers
//        ResponseEntity<DriverLocationDto[]> result = restTemplate.postForEntity(LOCATION_SERVICE + "/api/location/nearby/drivers",request,DriverLocationDto[].class);
//	
//        if(result.getStatusCode().is2xxSuccessful()&& result.getBody()!=null) {
//        List<DriverLocationDto> driverLocations = Arrays.asList(result.getBody());
//        driverLocations.forEach(driverLocationDto ->{
//        	System.out.println(driverLocationDto.getDriverId() + " " +"lat: "+driverLocationDto.getLatitude() +" "+"longi: "+driverLocationDto.getLongitude());
//        });
//        }
        
        return CreateBookingResponseDto.builder()
				.BookingId(newBooking.getId())
				.bookingStatus(newBooking.getBookingStatus().toString())
//	             .driver(Optional.of(newBooking.getDriver()))
	             .build();
	}

	public void processNearbyDriverAsync(NearbyDriversRequestDto requestDto,Long passengerId,CreateBookingDto bookingDetails,
			Long bookingId) {
		Call<DriverLocationDto[]> call =locationServiceApi.getNearbyDrivers(requestDto);
		call.enqueue(new Callback<DriverLocationDto[]>() {
			
			@Override
	   public void onResponse(Call<DriverLocationDto[]> call, Response<DriverLocationDto[]> response) {
             
           if (response.isSuccessful() && response.body() != null) {
           List<DriverLocationDto> driverLocations = Arrays.asList(response.body());
            driverLocations.forEach(driverLocationDto -> {
          System.out.println(driverLocationDto.getDriverId() + " " +
                           "lat: " + driverLocationDto.getLatitude() + " " +
                           "longi: " + driverLocationDto.getLongitude());
           
           
            ExactLocation startLoc = new ExactLocation(requestDto.getLatitude(), requestDto.getLongitude());
            ExactLocation endLoc = new ExactLocation(bookingDetails.getEndLocation().getLatitude(), bookingDetails.getEndLocation().getLongitude());
            

                RideRequestDto rideRequestDto = RideRequestDto.builder()
                    .passengerId(passengerId)
                    .startLocation(startLoc)
                    .endLocation(endLoc) // or a real destination if you have one
                   .driverIds(List.of(Long.parseLong(driverLocationDto.getDriverId())))  // only the current driver
                    .bookingId(bookingId)
                    .build();

                raiseRideRequestAsync(rideRequestDto);
            });
            }
               else {
        	    System.out.println("Request failed with status code: " + response.code());
        	}
		}
			
			@Override
			public void onFailure(Call<DriverLocationDto[]> call, Throwable t) {
				t.printStackTrace();
				
			}
		});
	}
	
	
	@Override
	public UpdateBookingResponseDto updateBooking(UpdateBookingRequestDto bookingRequestDto, Long bookingId) {
	     Optional<Long> driverId = bookingRequestDto.getDriverId();
         System.out.println("driver id "+ driverId);
	    if (driverId.isEmpty()) {
	        throw new IllegalArgumentException("Driver ID is required");
	    }
        
	    Long driverIds = driverId.get(); // Extract the Long value
	    Driver driver = driverRepository.findById(driverIds)
	            .orElseThrow(() -> new RuntimeException("Driver with ID " + driverId + " not found"));
	    //if(driver.isPrenset()&&driver.getisAvailable())
	    bookingRepository.updateBookingStatusAndDriverById(bookingId, BookingStatus.SCHEDULED, driver);
       //driverRepository.update to driver that he is unavailable currently to take another ride
	    Optional<Booking> booking = bookingRepository.findById(bookingId);

	    UpdateBookingResponseDto responseDto = UpdateBookingResponseDto.builder()
	            .bookingId(bookingId)
	            .status(booking.get().getBookingStatus())
	            .driver(Optional.ofNullable(booking.get().getDriver()))
	            .build();

	    // 🔥 Publish event for notification
	    try {
	        BookingConfirmedEvent event = new BookingConfirmedEvent();
	        event.setBookingId(bookingId);
	        event.setPassengerId(booking.get().getPassenger().getId());
	        event.setDriverId(driver.getId());
	        event.setDriverName(driver.getName());
	        event.setStatus("SCHEDULED");

	        String message = objectMapper.writeValueAsString(event);
	        kafkaTemplate.send("booking-confirmed-topic", message);
	        System.out.println("📤 Sent BookingConfirmedEvent: " + message);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return responseDto;
	}


	
	  public void raiseRideRequestAsync(RideRequestDto requestDto) {
		
		Call<Boolean> call = uberSocketApi.raiseRideRequest(requestDto);
		call.enqueue(new Callback<Boolean>() {

			@Override
			public void onResponse(Call<Boolean> call, Response<Boolean> response) {
				 if (response.isSuccessful() && response.body() != null) {
			           
			          Boolean result = response.body();
			          System.out.println("Driver resposne is "+result.toString());
				 } else {
			        	    System.out.println("Request failed with status code: " + response.code());
			        	}
				
			}

			@Override
			public void onFailure(Call<Boolean> call, Throwable t) {
				// TODO Auto-generated method stub
				t.printStackTrace();
			}
		});
	}
}
