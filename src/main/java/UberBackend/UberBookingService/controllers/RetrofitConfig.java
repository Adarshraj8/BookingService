package UberBackend.UberBookingService.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.discovery.EurekaClient;

import UberBackend.UberBookingService.apis.LocationServiceApi;
import UberBackend.UberBookingService.apis.UberSocketApi;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
@Configuration
public class RetrofitConfig {

	@Autowired
	private EurekaClient eurekaClient;
	
	private String getServiceUrl(String serviceName) {
		return eurekaClient.getNextServerFromEureka(serviceName, false).getHomePageUrl();
	}
	
	@Bean
	public LocationServiceApi locationServiceApi() {
		return new Retrofit.Builder()
				.baseUrl("http://localhost:1003/") // ✅ Valid URL
				.addConverterFactory(GsonConverterFactory.create())
				.client(new OkHttpClient.Builder().build())
				.build()
				.create(LocationServiceApi.class);
	}
	
	@Bean
	public UberSocketApi uberSocketApi() {
		return new Retrofit.Builder()
				.baseUrl("http://localhost:8080/") // ✅ Valid URL
				.addConverterFactory(GsonConverterFactory.create())
				.client(new OkHttpClient.Builder().build())
				.build()
				.create(UberSocketApi.class);
	}
}
