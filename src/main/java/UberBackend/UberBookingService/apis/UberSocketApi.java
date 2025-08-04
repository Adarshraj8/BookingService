package UberBackend.UberBookingService.apis;

import UberBackend.UberBookingService.dto.DriverLocationDto;
import UberBackend.UberBookingService.dto.NearbyDriversRequestDto;
import UberBackend.UberBookingService.dto.RideRequestDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UberSocketApi {

	@POST("/api/socket/newride")
    Call<Boolean> raiseRideRequest(@Body RideRequestDto request);
}
