package UberBackend.UberBookingService.apis;

import UberBackend.UberBookingService.dto.DriverLocationDto;
import UberBackend.UberBookingService.dto.NearbyDriversRequestDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LocationServiceApi {


    @POST("/api/location/nearby/drivers")
    Call<DriverLocationDto[]> getNearbyDrivers(@Body NearbyDriversRequestDto request);
}
