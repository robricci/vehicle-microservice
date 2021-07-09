package it.unisannio.vehicle;

import it.unisannio.vehicle.controller.VehicleController;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.ws.rs.ApplicationPath;

@SpringBootApplication
@EnableAsync
@EnableFeignClients
@EnableEurekaClient
@ApplicationPath("api")
public class VehicleApplication extends ResourceConfig {

	public VehicleApplication() {
		register(VehicleController.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(VehicleApplication.class, args);
	}

}
