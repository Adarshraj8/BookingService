package UberBackend.UberBookingService.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

import UberBackend.UberBookingService.apis.LocationServiceApi;
import UberBackend.UberBookingService.apis.UberSocketApi;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class RetrofitConfig {

    @Autowired
    private EurekaClient eurekaClient;

    private String getServiceUrl(String serviceName) {
        InstanceInfo instance = eurekaClient.getNextServerFromEureka(serviceName, false);
        String url = instance.getHomePageUrl();

        // Ensure it ends with /
        if (!url.endsWith("/")) {
            url += "/";
        }
        return url;
    }

    @Bean
    public LocationServiceApi locationServiceApi() {
        String baseUrl = getServiceUrl("UBERPROJECT-LOCATIONSERVICE"); // 🔥 Eureka resolves actual http://host:port/
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().build())
                .build()
                .create(LocationServiceApi.class);
    }

    @Bean
    public UberSocketApi uberSocketApi() {
        String baseUrl = getServiceUrl("UBERSOCKETSERVER");
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().build())
                .build()
                .create(UberSocketApi.class);
    }
}
