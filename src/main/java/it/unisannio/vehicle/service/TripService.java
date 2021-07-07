package it.unisannio.vehicle.service;

import it.unisannio.vehicle.dto.internal.StatisticsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(name = "trip-service")
public interface TripService {

    @GetMapping("/api/trips/statistics")
    StatisticsDTO getTripStatistics();
}
