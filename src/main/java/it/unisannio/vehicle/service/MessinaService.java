package it.unisannio.vehicle.service;

import it.unisannio.vehicle.dto.external.PredictionZonesResponse;
import it.unisannio.vehicle.dto.external.ZonesRequest;
import it.unisannio.vehicle.dto.external.ZonesResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@FeignClient(name = "messinaServiceFeignClient", url = "${api.external.messina.base-url}")
public interface MessinaService {

    @PostMapping("/getZonesByGeolocation")
    ZonesResponse getZonesByGeolocation(ZonesRequest zonesRequest);

    @GetMapping("/getPredictions")
    PredictionZonesResponse getPredictionPerZones();
}
